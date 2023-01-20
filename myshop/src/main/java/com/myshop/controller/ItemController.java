package com.myshop.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.myshop.dto.ItemFormDto;
import com.myshop.dto.ItemSearchDto;
import com.myshop.entity.Item;
import com.myshop.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;

	// 상품 능록 페이지를 보여줌
	@GetMapping(value = "/admin/item/new")
	public String itemForm(Model model) {
		model.addAttribute("itemFormDto", new ItemFormDto());
		return "item/itemForm";
	}

	// 상품등록
	@PostMapping(value = "/admin/item/new")
	public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, // controller控制层写参数接收的入口，需要注意的是@Valid
																						// 和 BindingResult 是一
																						// 一对应的，如果有多个@Valid，那么每个@Valid后面都需要添加BindingResult用于接收bean中的校验信息
			Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

		// bindingResult
		if (bindingResult.hasErrors()) {
			return "item/itemForm";
		}

		// first 이미지가 있는지 건사(first이미지는 필수 입력 값이기 때문
		if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫뻔째 상품 이미지는 필수 입력 값 입니다.");
			return "item/itemForm";
		}

		try {
			itemService.saveItem(itemFormDto, itemImgFileList);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
			return "item/itemForm";
		}
		return "redirect:/";
	}

	// 상품 수정 페이지 보기
	@GetMapping(value = "/admin/item/{itemId}") // localhost/admin/item/2
	public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
		try {
			ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
			model.addAttribute(itemFormDto);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
			model.addAttribute("itemFormDto", new ItemFormDto());
			return "item/itemForm";
		}
		return "item/itemForm";
	}

	// 상품 수정
	@PostMapping(value = "/admin/item/{itemId}")
	public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
			@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

		// bindingResult
		if (bindingResult.hasErrors()) {
			return "item/itemForm";
		}

		// first 이미지가 있는지 건사(first이미지는 필수 입력 값이기 때문
		if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫뻔째 상품 이미지는 필수 입력 값 입니다.");
			return "item/itemForm";
		}
		
		try {
			itemService.updateItem(itemFormDto, itemImgFileList);
			
		}catch (Exception e) {
			model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
			return "item/itemForm";
		}
		return "redirect:/";
	}

	@GetMapping(value = {"/admin/items", "/admion/items/{page}"})  //페이지 번호가 없는 경우와 있는 경우 2가지를 매핑
	public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
		
		//url경로에 페이지가 있으명 해당 페이지를 조회하도록 하고 페이지 번호가 없다몀 0페이지를 조회하도록 한다. 
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);  //of()
		
		Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
		
		model.addAttribute("items",items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);
		
		return "item/itemMng";
		}
}
