package com.ecommerce.mazdacart.controllers;

import com.ecommerce.mazdacart.payload.AddressDTO;
import com.ecommerce.mazdacart.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
public class AddressController {

	@Autowired
	AddressService addressService;

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PostMapping("/public/address/create")
	public ResponseEntity<AddressDTO> createAddress (@Valid @RequestBody AddressDTO addressDTO) {
		AddressDTO responseDTO = addressService.createNewAddress(addressDTO);
		return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/admin/address")
	public ResponseEntity<List<AddressDTO>> getAllAddresses () {
		List<AddressDTO> responseDTO = addressService.getAllAddresses();
		return ResponseEntity.ok().body(responseDTO);
	}


	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/public/address/user")
	public ResponseEntity<List<AddressDTO>> getAllAddressesForUser () {
		List<AddressDTO> responseDTO = addressService.getAllAddressesForUser();
		return ResponseEntity.ok().body(responseDTO);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@PutMapping("/public/address/update/{addressId}")
	public ResponseEntity<AddressDTO> updateUserAddress (@Valid @RequestBody AddressDTO addressDTO,
	                                                     @NotNull @PathVariable Long addressId) {
		AddressDTO responseDTO = addressService.updateUserAddress(addressDTO, addressId);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@DeleteMapping("/public/address/delete/{addressId}")
	public ResponseEntity<AddressDTO> deleteAddress (@NotNull @PathVariable Long addressId) {
		AddressDTO responseDTO = addressService.deleteAddress(addressId);
		return ResponseEntity.ok().body(responseDTO);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping("/admin/address/delete/{addressId}")
	public ResponseEntity<AddressDTO> deleteAnyAddress (@NotNull @PathVariable Long addressId) {
		AddressDTO responseDTO = addressService.deleteAnyAddress(addressId);
		return ResponseEntity.ok().body(responseDTO);
	}
}
