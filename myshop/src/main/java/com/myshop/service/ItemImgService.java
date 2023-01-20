package com.myshop.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.groovy.parser.antlr4.util.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myshop.entity.ItemImg;
import com.myshop.repository.ItemImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

	@Value("${itemImgLocation}")
	private String itemImgLocation;
	
	private final ItemImgRepository itemImgRepository;
	
	private final FileService fileService;
	
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
		String oriImgName = itemImgFile.getOriginalFilename();  //파일 이름
		String imgName = "";
		String imgUrl = "";
		
		
		//파일 업로드 
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/images/item/" + imgName;
		}
		
		//상품 이미지 정보 저장
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(itemImg);
	}
	
	
	//이미지 업데이트 메소드
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
		if(!itemImgFile.isEmpty()) { //파일이 있으면
			ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
					.orElseThrow(EntityNotFoundException::new);
			
			//기존 이미지 삭제
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName()); 
			}
			
			String oriImgName = itemImgFile.getOriginalFilename();  //파일 이름
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			String imgUrl = "/images/item/" + imgName;
			
			//******savedItemImg는 현재 영속상태 데이트를 변경하는 것만으로 변경감지 기능이 동작하여 트렌잭션이 끝날대 UPDATE 쿼리가 실행
			//->entity 반드시 영속상태여야 한다.
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
			
		}
	}
}
