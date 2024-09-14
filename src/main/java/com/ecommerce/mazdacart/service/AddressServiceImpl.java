package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.exceptions.APIException;
import com.ecommerce.mazdacart.exceptions.ResourceNotFoundException;
import com.ecommerce.mazdacart.model.Address;
import com.ecommerce.mazdacart.model.Users;
import com.ecommerce.mazdacart.payload.AddressDTO;
import com.ecommerce.mazdacart.repository.AddressRepository;
import com.ecommerce.mazdacart.repository.UserRepository;
import com.ecommerce.mazdacart.util.AuthUtilHelperClass;
import com.ecommerce.mazdacart.util.EcomConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthUtilHelperClass authUtilHelperClass;

	@Autowired
	private UserRepository userRepository;

	@Override
	public AddressDTO createNewAddress (AddressDTO addressDTO) {
		Address address = modelMapper.map(addressDTO, Address.class);
		Address existingAddress =
			addressRepository.findIfAddressExists(address.getCity(), address.getState(), address.getStreet(),
				address.getCountry(), address.getBuildingName(), address.getZipcode());

		Users currentUser = authUtilHelperClass.getCurrentUser();

		if (existingAddress != null && currentUser.getAddresses().stream().anyMatch(
			add -> add.getAddressId().equals(existingAddress.getAddressId()))) {
			throw new APIException("Given address already exists for the user");
		}


		address.setUsers(currentUser);
		Address savedAddress = addressRepository.save(address);
		return modelMapper.map(savedAddress, AddressDTO.class);
	}

	@Override
	public List<AddressDTO> getAllAddresses () {
		List<Address> allAddresses = addressRepository.findAll();
		if (allAddresses.isEmpty()) {
			throw new ResourceNotFoundException("No addresses found in system");
		}
		return allAddresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
	}

	@Override
	public List<AddressDTO> getAllAddressesForUser () {
		Users currentUser = authUtilHelperClass.getCurrentUser();
		List<Address> addresses = currentUser.getAddresses();
		if (addresses.isEmpty()) {
			throw new ResourceNotFoundException(
				EcomConstants.NO_ADDRESSES_FOUND_FOR_THE_USER + currentUser.getUserName());
		}
		return addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class)).toList();
	}

	@Override
	public AddressDTO updateUserAddress (AddressDTO addressDTO, Long addressId) {
		Users currentUser = authUtilHelperClass.getCurrentUser();
		List<Address> addresses = currentUser.getAddresses();
		if (addresses.isEmpty()) {
			throw new ResourceNotFoundException(
				EcomConstants.NO_ADDRESSES_FOUND_FOR_THE_USER + currentUser.getUserName());
		}

		Address addressToUpdate = addresses.stream().filter(add -> add.getAddressId().equals(addressId)).findAny()
			                          .orElseThrow(() -> new ResourceNotFoundException(
				                          EcomConstants.NO_ADDRESSES_FOUND_FOR_THE_USER + currentUser.getUserName() +
					                          " for the given addressId" + addressId));
		addressToUpdate.setCity(addressDTO.getCity());
		addressToUpdate.setState(addressDTO.getState());
		addressToUpdate.setCountry(addressDTO.getCountry());
		addressToUpdate.setZipcode(addressDTO.getZipcode());
		addressToUpdate.setBuildingName(addressDTO.getBuildingName());
		addressToUpdate.setStreet(addressDTO.getStreet());
		return modelMapper.map(addressRepository.save(addressToUpdate), AddressDTO.class);
	}

	@Override
	public AddressDTO deleteAddress (Long addressId) {
		Users currentUser = authUtilHelperClass.getCurrentUser();
		List<Address> addresses = currentUser.getAddresses();
		if (addresses.isEmpty()) {
			throw new ResourceNotFoundException(
				EcomConstants.NO_ADDRESSES_FOUND_FOR_THE_USER + currentUser.getUserName());
		}

		Address addressToDelete = addresses.stream().filter(add -> add.getAddressId().equals(addressId)).findAny()
			                          .orElseThrow(() -> new ResourceNotFoundException(
				                          EcomConstants.NO_ADDRESSES_FOUND_FOR_THE_USER + currentUser.getUserName() +
					                          " for the given addressId: " + addressId));
		currentUser.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
		userRepository.save(currentUser);
		addressRepository.delete(addressToDelete);
		return modelMapper.map(addressToDelete, AddressDTO.class);
	}

	@Override
	public AddressDTO deleteAnyAddress (Long addressId) {
		Address addressToDelete = addressRepository.findById(addressId).orElseThrow(
			(() -> new ResourceNotFoundException("No Address found for the given addressId: " + addressId)));
		Users user = addressToDelete.getUsers();
		user.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
		userRepository.save(user);
		addressRepository.delete(addressToDelete);
		return modelMapper.map(addressToDelete, AddressDTO.class);
	}
}
