const express = require('express');
const { MongoClient } = require('mongodb');
const https = require('https');
const axios = require('axios');
const mongoose = require('mongoose');
const { Eureka } = require('eureka-js-client');

const app = express();

// Read config from config.json
const configUrl = 'https://raw.githubusercontent.com/hamedkar/sahred/main/config.json';

https.get(configUrl, (res) => {
  let rawData = '';
  res.on('data', (chunk) => {
    rawData += chunk;
  });
  res.on('end', () => {
    try {
      const config = JSON.parse(rawData);
      const { mongoURI, port } = config;
      startServer(mongoURI, port);
    } catch (error) {
      console.error(error);
    }
  });
}).on('error', (error) => {
  console.error(error);
});
// Eureka configuration
const eurekaConfig = {
  eureka: {
    host: 'localhost',
    port: 8761,
    servicePath: '/eureka/apps/',
    heartbeatInterval: 5000,
    registryFetchInterval: 5000,
    preferIpAddress: true
  },
  instance: {
    app: 'ms-mag',
    hostName: 'localhost',
    ipAddr: '127.0.0.1',
    port: {
      '$': 3000,
      '@enabled': 'true'
    },
    vipAddress: 'YOUR_APP_NAME',
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn'
    }
  }
};

const eurekaClient = new Eureka(eurekaConfig);



async function startServer(mongoURI, port) {
  const client = new MongoClient(mongoURI, { useUnifiedTopology: true });

  // Define magasin schema
  const magasinSchema = new mongoose.Schema({
    name: { type: String, required: true },
    id: { type: String, required: true },
    location: { type: String, required: true },
    stock: [{ type: String }] // Array of stock strings
  });

  // Create magasin model
  const Magasin = mongoose.model('Magasin', magasinSchema);

  app.use(express.json());

  app.get('/', (req, res) => {
    res.send('Hello, Express!');
  });

  app.get('/magasins', async (req, res) => {
    try {
      await client.connect();
      const db = client.db();
      const magasins = db.collection('magasins');
      const magasinsList = await magasins.find().toArray();
      res.json(magasinsList);
    } catch (err) {
      console.error(err);
      res.status(500).send('Internal Server Error');
    } finally {
      await client.close();
    }
  });

  app.post('/add-magasin', async (req, res) => {
    try {
      await client.connect();
      const db = client.db();
      const magasins = db.collection('magasins');
      const { name, id, location, stock } = req.body;
      const newMagasin = new Magasin({ name, id, location, stock });
      const result = await magasins.insertOne(newMagasin);
      res.json(result);
    } catch (err) {
      console.error(err);
      res.status(500).send('Internal Server Error');
    } finally {
      await client.close();
    }
  });

  app.get('/get-stock', async (req, res) => {
    try {
      const stockResponse = await axios.get('http://localhost:8090/stock/getStock/1');
      const stock = stockResponse.data;
      res.json(stock);
    } catch (error) {
      console.error('Error retrieving stock:', error);
      res.status(500).send('Internal Server Error');
    }
  });

  app.get('/magasins/:id', async (req, res) => {
    try {
      await client.connect();
      const db = client.db();
      const magasins = db.collection('magasins');
      const { id } = req.params;
      console.log(id);
      const magasin = await magasins.findOne({ id });

      if (!magasin) {
        res.status(404).json({ error: 'Magasin not found' });
        return;
      }

      const stock = [];
      for (const id of magasin.stock) {
        try {
          const stockResponse = await axios.get(`http://localhost:8090/stock/getStock/${id}`);
          stock.push(stockResponse.data);
        } catch (error) {
          console.error(`Error retrieving stock for stock ${id}:`, error);
          stock.push({ id, quantity: 'Stock not found' }); // Add stock not found message to the response
        }
      }

      res.json({ magasin, stock });
    } catch (err) {
      console.error(err);
      res.status(500).send('Internal Server Error');
    } finally {
      await client.close();
    }
  });

  async function createUniqueIndexes() {
    try {
      await client.connect();
      const db = client.db();
      const magasins = db.collection('magasins');
      await magasins.createIndex({ name: 1 }, { unique: true });
      await magasins.createIndex({ id: 1 }, { unique: true });
    } catch (err) {
      console.error('Failed to create unique indexes:', err);
    } finally {
      await client.close();
    }
  }

  createUniqueIndexes();

  // Start the server
  app.listen(port, () => {
    console.log(`Server listening on port ${port}`);
    eurekaClient.start((error) => {
      if (error) {
        console.error('Error registering with Eureka:', error);
      } else {
        console.log('Registered with Eureka server');
      }
    });
  });
}
