package com.myshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;

//JpaRepository:기본적인 crud 및 페이징 처리를 위한 메소드가 정의가 되어었다.
//JpaRepository<사용할 엔티티 클래스, 기본키 타입>
public interface ItemRepository extends JpaRepository<Item, Long>{
	//select * from item where item_nm = ?;
	List<Item> findByItemNm(String itemNm); 
	
	//select * from item where item_nm = ? or item_detail = ?;
	List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
	
	//select * from item where price < ?
	List<Item> findByPriceLessThan(Integer price);
	
	//select * from item where price < ? order by price desc;
	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
	
	List<Item> findByItemNmAndItemSellStatus(String itemNm, ItemSellStatus itemSellStatus);
	
	List<Item> findByPriceBetween(Integer price, Integer price1);
	
	List<Item> findByRegTimeAfter(LocalDateTime regTime);
	
	List<Item> findByItemSellStatusNotNull();
	
	List<Item> findByItemDetailEndingWith(String itemDetail);
}
