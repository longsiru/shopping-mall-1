package com.myshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.myshop.constant.ItemSellStatus;
import com.myshop.dto.ItemSearchDto;
import com.myshop.entity.Item;
import com.myshop.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

	private JPAQueryFactory queryFactory;
	
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();
		
		//현재 날짜로 이전 날짜를 구해준다.
		if(StringUtils.equals("all", searchDateType) || searchDateType == null) return null;
		else if(StringUtils.equals("1d", searchDateType)) dateTime = dateTime.minusDays(1);
		else if(StringUtils.equals("1w", searchDateType)) dateTime = dateTime.minusWeeks(1);
		else if(StringUtils.equals("1m", searchDateType)) dateTime = dateTime.minusMonths(1);
		else if(StringUtils.equals("6m", searchDateType)) dateTime = dateTime.minusMonths(6);
		
		return QItem.item.regTime.after(dateTime);  //이후의 시간
	}
	
	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
	}
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("itemNm", searchBy)) {
			return QItem.item.itemNm.like("%" + searchQuery + "%");   //item LIKE %청바진%
		}else if(StringUtils.equals("createdBy", searchBy)) {
			return QItem.item.createdBy.like("%" + searchQuery + "%");  //createdBy LIKE %test.com%
		}
		return null;
	}
	
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		List<Item> content = queryFactory
				.selectFrom(QItem.item) //select * item
				.where(regDtsAfter(itemSearchDto.getSearchDateType()), //where reg_tiem = 1d
						searchSellStatusEq(itemSearchDto.getSearchSellStatus()),  //and sell_status = ?
						searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))  //and itemNm LIKE %건색어%
				.orderBy(QItem.item.id.desc())
				.offset(pageable.getOffset())//데이터를 가져올 시작 index
				.limit(pageable.getPageSize()) //한번에 가지고 올 최대 개수 
				.fetch();
			
		long total = content.size();
		
		return new PageImpl<>(content, pageable, total);
		
		
		
	}
	
}
