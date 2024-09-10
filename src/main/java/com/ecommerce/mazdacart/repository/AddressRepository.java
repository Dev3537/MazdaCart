package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query(nativeQuery = true, value =
		                           "select a.* from address a where  lower(a.city) =lower(:city) and lower(a" +
			                           ".state) =lower(:state) and  lower(a.street) =lower(:street) and  " +
			                           "lower(a.country) =lower(:country) and lower(a.building_name) =lower" +
			                           "(:buildingName) and lower(a.zipcode) =lower(:zipcode)  ")
	Address findIfAddressExists (String city, String state, String street, String country, String buildingName,
	                             String zipcode);
}
