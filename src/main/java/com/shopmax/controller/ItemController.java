package com.shopmax.controller;

import java.util.List;
import java.util.Optional;

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

import com.shopmax.dto.ItemFormDto;
import com.shopmax.dto.ItemSearchDto;
import com.shopmax.dto.MainItemDto;
import com.shopmax.entity.Item;
import com.shopmax.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	// 상품전체 리스트
	@GetMapping(value = "/item/shop")
	public String itemshopList(Model model, ItemSearchDto itemSearchDto, Optional<Integer> page) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

		model.addAttribute("items", items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);

		return "/item/itemShopList";
	}

	// 상품 상세 페이지
	@GetMapping(value = "/item/{itemId}")
	public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
		ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
		model.addAttribute("item", itemFormDto);
		return "item/itemDtl";
	}

	// 상품등록 페이지
	@GetMapping(value = "/admin/item/new")
	public String itemForm(Model model) {
		model.addAttribute("itemFormDto", new ItemFormDto());
		return "item/itemForm";
	}

	// 상품 등록( insert)
	@PostMapping(value = "/admin/item/new")
	public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
			@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

		if (bindingResult.hasErrors()) {
			return "item/itemForm";
		}

		// 상품등록전에 첫번째 이미지가 있는지 없는지 검사(첫번쨰 이미지는 필수값)
		if (itemImgFileList.get(0).isEmpty()) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
			return "item/itemForm";
		}

		try {
			itemService.saveItem(itemFormDto, itemImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품등록중 에러가 발생했습니다.");
			return "item/itemForm";
		}

		return "redirect:/";

	}

	// 상품관리 페이지
	@GetMapping(value = { "/admin/items", "/admin/items/{page}" })
	public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {

		// url 경로에 페이지가 있으면 해당 페이지 번호를 조회하도록 하고 페이지 번호가 없으면 0페이지 조회
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); // of(조회 할 페이지의 번호, 한페이지당 조회할 데이터 갯수)

		Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
		model.addAttribute("items", items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5); // 상품관리 페이지 하단에 보여줄 최대 페이지번호

		return "item/itemMng";
	}

	// 상품 수정페이지 보기
	@GetMapping(value = "/admin/item/{itemId}")
	public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {

		try {
			ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
			model.addAttribute("itemFormDto", itemFormDto);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품정보를 가져올때 에러가 발생했브니다.");
			model.addAttribute("itemFormDto", new ItemFormDto());
			return "item/itemForm";
		}

		return "item/itemModifyForm";
	}

	// 상품 수정(update)
	@PostMapping(value = "/admin/item/{itemId}")
	public String itemUpdate(@Valid ItemFormDto itemFormDto, Model model, BindingResult bindingResult,
			@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

		System.out.println(itemFormDto.getId() + "ttttttttttttt");
		if (bindingResult.hasErrors()) {
			return "item/itemFotm";
		}

		// 첫번째 이미지가 있는지 검사
		if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수입니다.");
			return "item/itemForm";
		}

		try {
			itemService.UpdateItem(itemFormDto, itemImgFileList);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "상품수정 중 에러가 발생했습니다");
			return "item/itemForm";
		}

		return "redirect:/";
	}

}