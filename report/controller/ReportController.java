package com.supermarket.finance.report.controller;

import com.supermarket.finance.common.Result;
import com.supermarket.finance.report.entity.FinBalanceSheet;
import com.supermarket.finance.report.entity.FinCashFlowStatement;
import com.supermarket.finance.report.entity.FinIncomeStatement;
import com.supermarket.finance.report.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/is")
    @PreAuthorize("hasAuthority('report:generate') or hasRole('ADMIN')")
    public Result<FinIncomeStatement> generateIS(@RequestParam Long bookId,
                                                 @RequestParam String period,
                                                 @RequestParam(required = false) String maker) {
        return Result.success(reportService.generateIncomeStatement(bookId, period, maker));
    }

    @PostMapping("/bs")
    @PreAuthorize("hasAuthority('report:generate') or hasRole('ADMIN')")
    public Result<FinBalanceSheet> generateBS(@RequestParam Long bookId,
                                              @RequestParam String period,
                                              @RequestParam(required = false) String maker) {
        return Result.success(reportService.generateBalanceSheet(bookId, period, maker));
    }

    @PostMapping("/cf")
    @PreAuthorize("hasAuthority('report:generate') or hasRole('ADMIN')")
    public Result<FinCashFlowStatement> generateCF(@RequestParam Long bookId,
                                                   @RequestParam String period,
                                                   @RequestParam(required = false) String maker) {
        return Result.success(reportService.generateCashFlowStatement(bookId, period, maker));
    }

    @GetMapping("/is")
    @PreAuthorize("hasAuthority('report:view') or hasRole('ADMIN')")
    public Result<FinIncomeStatement> getIS(@RequestParam(required = false) Long bookId,
                                            @RequestParam String period) {
        return Result.success(reportService.getIncomeStatement(defaultBookId(bookId), period));
    }

    @GetMapping("/bs")
    @PreAuthorize("hasAuthority('report:view') or hasRole('ADMIN')")
    public Result<FinBalanceSheet> getBS(@RequestParam(required = false) Long bookId,
                                         @RequestParam String period) {
        return Result.success(reportService.getBalanceSheet(defaultBookId(bookId), period));
    }

    @GetMapping("/cf")
    @PreAuthorize("hasAuthority('report:view') or hasRole('ADMIN')")
    public Result<FinCashFlowStatement> getCF(@RequestParam(required = false) Long bookId,
                                              @RequestParam String period) {
        return Result.success(reportService.getCashFlowStatement(defaultBookId(bookId), period));
    }

    /**
     * 导出 PDF（单文件，包含 IS/BS/CF）。
     * 说明：PDF 使用外部中文字体（通过环境变量 REPORT_PDF_FONT_PATH 指定字体文件路径）。
     */
    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAuthority('report:view') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportPdf(@RequestParam(required = false) Long bookId,
                                            @RequestParam String period) {
        String operator = currentUsernameOrSystem();
        byte[] pdf = reportService.exportReportsPdf(defaultBookId(bookId), period, operator);
        String filename = "reports-" + period + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private static Long defaultBookId(Long bookId) {
        return bookId == null ? 1L : bookId;
    }

    private static String currentUsernameOrSystem() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return "system";
        String p = String.valueOf(auth.getPrincipal());
        if ("anonymousUser".equalsIgnoreCase(p)) return "system";
        return p;
    }
}





