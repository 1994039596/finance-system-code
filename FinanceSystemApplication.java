package com.supermarket.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 扫描Mapper包（核心：无需给Mapper加@Mapper注解）
@MapperScan({
		"com.supermarket.finance.mapper",
		"com.supermarket.finance.purchase.mapper",
		"com.supermarket.finance.supplier.mapper",
		"com.supermarket.finance.gl.mapper",
		"com.supermarket.finance.payment.mapper",
		"com.supermarket.finance.customer.mapper",
		"com.supermarket.finance.bank.mapper",
		"com.supermarket.finance.report.mapper",
		"com.supermarket.finance.tax.mapper",
		"com.supermarket.finance.hr.mapper"
})
@SpringBootApplication
public class FinanceSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(FinanceSystemApplication.class, args);
	}
}