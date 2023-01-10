package com.myshop.entity;

import java.time.LocalDateTime;

import javax.persistence.*;


import com.myshop.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="item")  //table명
@Getter
@Setter
@ToString
public class Item {
	//NOT NULL 아니면 필드 TYPE는을 객제(예:INTT --INTRGER)로 지정해야 한다.
	
	@Id
	@Column(name="item_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id; //상품코드
	
	
	@Column(nullable = false, length = 50)
	private String itemNm;//상품명
	
	@Column(nullable = false, name = "price")
	private int price;//가격
	
	@Column(nullable = false)
	private int stockNumber;//재고수량
	
	@Lob
	@Column(nullable = false)
	private String itemDetail;// 상품 상세성명
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;//상품 판매상태
	
	
	private LocalDateTime regTime;//등록시간
	
	
	private LocalDateTime updateTime;//수정시간
	 
}
