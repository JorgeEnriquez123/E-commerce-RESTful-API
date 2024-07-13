package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddressLineDto;
import com.jorge.ecommerce.model.AddressLine;
import com.jorge.ecommerce.repository.AddressLineRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressLineService {
    private final AddressLineRepository repository;
    private final ModelMapper modelMapper;

    public List<AddressLineDto> getAddressesByUserId(Long id) {
        List<AddressLine> addressLines = repository.findByUserId(id).orElseThrow();
        List<AddressLineDto> addressLineDtos = new ArrayList<>();
        addressLines.forEach(
                addressLine -> {
                    AddressLineDto addressLineDto = modelMapper.map(addressLine, AddressLineDto.class);
                }
        );
        return addressLineDtos;
    }

    public AddressLineDto saveAddressLineWithUserId(Long idUser, AddressLineDto addressLineDto) {
        AddressLine newAddressLine = modelMapper.map(addressLineDto, AddressLine.class);
        newAddressLine.setId(idUser);
        repository.save(newAddressLine);
        return modelMapper.map(newAddressLine, AddressLineDto.class);
    }

    public AddressLineDto updateAddressLine(Long idAddressLine, AddressLineDto addressLineDto) {
        AddressLine addressLine = repository.findById(idAddressLine).orElseThrow();
        addressLineDto.setId(idAddressLine);
        modelMapper.map(addressLineDto, addressLine);
        repository.save(addressLine);
        return modelMapper.map(addressLine, AddressLineDto.class);
    }
}
