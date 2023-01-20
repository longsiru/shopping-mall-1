
package com.myshop.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.myshop.constant.ItemSellStatus;
import com.myshop.entity.Item;

//JpaRepository:기본적인 crud 및 페이징 처리를 위한 메소드가 정의가 되어었다.
//JpaRepository<사용할 엔티티 클래스, 기본키 타입>
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
	//select * from item where item_nm = ?
	List<Item> findByItemNm(String itemNm); 
	
	//select * from item where item_nm = ? or item_detail = ?;
	List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
	
	//select * from item where price < ?
	List<Item> findByPriceLessThan(Integer price);
	
	//select * from item where price < ? order by price desc;
	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
	
	//quiz
	//List<Item> findByItemNmAndItemSellStatus(String itemNm, ItemSellStatus itemSellStatus);
	
	//List<Item> findByPriceBetween(Integer price, Integer price1);
	
	//List<Item> findByRegTimeAfter(LocalDateTime regTime);
	
	//List<Item> findByItemSellStatusNotNull();  //List<Item> findByItemSellStatusNull();
	
	//List<Item> findByItemDetailEndingWith(String itemDetail); //List<Item> findByItemDetailLike(String itemDetail);
	
	//@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")  //아래 입력한 값을 들어가.//table name is entity's name.
	//List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
	
	@Query("select i from Item i where i.itemDetail like %?1% order by i.price desc")  // %?1%: @Param("itemDetail") 의 순서
	List<Item> findByItemDetail(String itemDetail);
	
	@Query(value = "select * from Item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
	
	//@Query(value = "select * from Item i where i.price >= ?1 ", nativeQuery = true)
	//List<Item> findByPriceByNative(@Param("price") Integer price);
	
	//@Query(value = "select * from Item i where i.item_nm = :itemNm and i.item_sell_status = :#{#sell.name()}", nativeQuery = true)
	//List<Item> findByItemNmAndItemSellStatusByNative(@Param("itemNm") String itemNm, @Param("sell") ItemSellStatus sell);
	
	//@Query(value = "select * from Item i where i.item_nm = :itemNm and i.item_sell_status = :itemSellStatus", nativeQuery = true)
	//List<Item> findByItemNmAndItemSellStatusByNative(@Param("itemNm") String itemNm, @Param("itemSellStatus") String itemSellStatus);
	
	//@Query("select i from Item i where i.itemNm = :itemNm and i.itemSellStatus = :itemSellStatus")
	//List<Item> findByItemNmAndItemSellStatus(String itemNm, ItemSellStatus itemSellStatus);
	
}
