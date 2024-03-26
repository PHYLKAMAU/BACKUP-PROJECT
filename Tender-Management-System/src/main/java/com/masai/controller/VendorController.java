package com.masai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.masai.exception.BidException;
import com.masai.exception.NotFoundException;
import com.masai.exception.TenderException;
import com.masai.exception.VendorException;
import com.masai.model.Bid;
import com.masai.model.Tender;
import com.masai.model.Vendor;
import com.masai.service.VendorService;

@RestController
@CrossOrigin(origins = "*")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@PostMapping("/vendors")
	public ResponseEntity<Vendor> registerVendorHandler(@RequestBody Vendor vendor) throws VendorException {
		Vendor savedVendor = vendorService.createVendor(vendor);
		return new ResponseEntity<>(savedVendor, HttpStatus.CREATED);
	}

	@PutMapping("/{username}/{password}/{newPassword}")
	public ResponseEntity<String> updateVendorPassword(@PathVariable("username") String username,
			@PathVariable("password") String password, @PathVariable("newPassword") String newPassword)
			throws VendorException {
		vendorService.updateVendorPassword(username, password, newPassword);
		return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
	}

	@GetMapping("/available tenders")
	public ResponseEntity<List<Tender>> getAllTenders() throws TenderException {
		List<Tender> tenders = vendorService.viewAllTenders();
		if (tenders.size() == 0) {
			throw new TenderException("Tenders are not available");
		}
		return new ResponseEntity<>(tenders, HttpStatus.OK);
	}

	@GetMapping("/availableTenders/{id}")
	public ResponseEntity<Tender> viewTendersByIdHandler(@PathVariable("id") Integer id)
			throws TenderException, NotFoundException {

		Tender tender = vendorService.viewTendersById(id);

		return new ResponseEntity<>(tender, HttpStatus.FOUND);

	}


	
	@PostMapping("/tenders/{tenderId}/{vendorId}")
	public ResponseEntity<Bid> placeBid(@PathVariable Integer tenderId, @PathVariable Integer vendorId,
			@RequestBody Bid bid) throws TenderException, VendorException, BidException {
		Bid placedBid = vendorService.placeBid(tenderId, vendorId, bid);
		return new ResponseEntity<>(placedBid, HttpStatus.CREATED);
	}

	@GetMapping("/vendors/bidHistory/{vendorId}")
	public ResponseEntity<List<Bid>> viewBidHistoryHandler(@PathVariable Integer vendorId) throws NotFoundException, VendorException {
		List<Bid> bidHistory = vendorService.viewBidHistory(vendorId);
		return new ResponseEntity<>(bidHistory, HttpStatus.OK);
	}

}
