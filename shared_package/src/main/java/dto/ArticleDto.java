package dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class ArticleDto {
    public Long id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String name;
    private int qte;
    private StockDto stockDto;
}
