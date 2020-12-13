package com.java.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "http://ACTIVITYTRACKERSERVICE/employee")
public interface ActivityServiceClient {

	@GetMapping("/port")
	public String getInfo();

	/*
	 * @PostMapping("/fundtransfer") public String fundTransfer(@RequestBody
	 * TransactionMasterRequest transreq);
	 */
}