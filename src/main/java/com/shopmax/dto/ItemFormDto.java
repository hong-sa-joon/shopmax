package com.shopmax.dto;

import com.shopmax.constant.ItemSellStatus;
import com.shopmax.entity.Item;
import com.shopmax.entity.ItemImg;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력입니다.")
    private int price;
    @NotNull(message = "재고는 필수 입력입니다")
    private int stockNumber;
    @NotBlank(message = "상품 상세설명은 필수 입력입니다.")
    private String itemDetail;
    private ItemSellStatus itemSellStatus;
    //상품 1개당 5개의 이미지를 저장하므로 아래와 같이 리스트로 작성
    //상품 이미지 정보를 저장
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    //상품 이미지 아이디들을 저장 -> 수정시 이미지 아이디들을 담아 둘 용도
    private List<Long> itemImgIds = new ArrayList<>();

    //modelMapper를 사용
    private static ModelMapper modelMapper = new ModelMapper();

    //dto -> entity
    public Item createItem(){
        return modelMapper.map(this,Item.class);//itementity를 리턴

    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);//itemFormDto를 리턴
    }

}
