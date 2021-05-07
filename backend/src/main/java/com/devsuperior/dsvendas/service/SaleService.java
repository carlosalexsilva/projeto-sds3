package com.devsuperior.dsvendas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsvendas.dto.SaleDTO;
import com.devsuperior.dsvendas.dto.SaleSuccessDTO;
import com.devsuperior.dsvendas.dto.SaleSumDTO;
import com.devsuperior.dsvendas.entities.Sale;
import com.devsuperior.dsvendas.repositories.SaleRepository;
import com.devsuperior.dsvendas.repositories.SellerRepository;

@Service
public class SaleService {
	//injeta automaticamente o repository, não precisando instanciar.
	@Autowired
	private SaleRepository repository;
	
	@Autowired
	private SellerRepository sellerRepository;
	
	@Transactional(readOnly = true) //para não fazer lock de escrita no banco, pois é so leitura
	public Page<SaleDTO> findAll(Pageable pageable) {
		//para esse projeto, como tem poucos vendedores, foi carregado agora todos os vendedores e assim eles ficaram em cache
		// de modo que não vai ficar fazendo um select para cada venda, pois ele irá buscar do cache.
		sellerRepository.findAll();
		
		Page<Sale> result = repository.findAll(pageable);
		//convert result Sale em SaleDTO, não precisa usar o stream, pois o page já e uma coleção de dados
		return result.map(x -> new SaleDTO(x));
	}
	
	@Transactional(readOnly = true)
	public List<SaleSumDTO> amountGroupedBySeller() {
		return repository.amountGroupedBySeller();
	}
	
	@Transactional(readOnly = true)
	public List<SaleSuccessDTO> successGroupedBySeller() {
		return repository.successGroupedBySeller();
	}
}
