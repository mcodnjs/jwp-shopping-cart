package cart.controller.dto;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductRequest {

    @NotBlank(message = "상품 이름은 공백을 입력할 수 없습니다.")
    @Size(min = 1, max = 10, message = "상품 이름은 1자 이상 10자 이하만 가능합니다.")
    private final String name;

    @NotBlank(message = "상품 사진의 url은 공백을 입력할 수 없습니다.")
    @URL(message = "유효하지 않은 상품 사진 URL 입니다.")
    private final String imageUrl;

    @NotNull(message = "상품 가격은 필수 값입니다.")
    @Range(min = 1, max = 100_000_000, message = "상품 금액은 1원 이상 100,000,000원 이하만 가능합니다.")
    private final Integer price;

    public ProductRequest(final String name, final String imageUrl, final Integer price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPrice() {
        return price;
    }
}
