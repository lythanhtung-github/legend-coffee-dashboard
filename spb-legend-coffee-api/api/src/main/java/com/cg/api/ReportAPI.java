package com.cg.api;

import com.cg.domain.dto.report.ReportDayToDayDTO;
import com.cg.domain.dto.report.ReportDTO;
import com.cg.domain.dto.report.ReportProductDTO;
import com.cg.domain.dto.report.ReportYearDTO;
import com.cg.exception.DataInputException;
import com.cg.service.order.IOrderService;
import com.cg.service.orderItem.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportAPI {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;


    @GetMapping("/day/{day}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getReportOfDay(@PathVariable String day) {

        List<ReportDTO> report = orderService.getReportOfDay(day);

        if (report.size() == 0) {
            throw new DataInputException("Ngày " + day + " chưa có doanh thu!");
        }

        return new ResponseEntity<>(report.get(0).getTotalAmount(), HttpStatus.OK);
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getReportByYear(@PathVariable int year) {

        List<ReportYearDTO> reportMonth = orderService.getReportByYear(year);

        if (reportMonth.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportMonth, HttpStatus.OK);
    }

    @GetMapping("/day/{startDay}/{endDay}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getReportFromDayToDay(@PathVariable String startDay, @PathVariable String endDay) {

        String[] startDayArray = startDay.split("-");
        String[] endDayArray = endDay.split("-");

        int startDayTemp = Integer.parseInt(startDayArray[startDayArray.length - 1]) - 1;
        if (startDayTemp < 10)
            startDayArray[startDayArray.length - 1] = "0" + startDayTemp;
        else
            startDayArray[startDayArray.length - 1] = String.valueOf(startDayTemp);

        startDay = String.join("-", startDayArray);

        int endDayTemp = Integer.parseInt(endDayArray[endDayArray.length - 1]) + 1;
        if (endDayTemp < 10)
            endDayArray[endDayArray.length - 1] = "0" + endDayTemp;
        else
            endDayArray[endDayArray.length - 1] = String.valueOf(endDayTemp);

        endDay = String.join("-", endDayArray);

        List<ReportDayToDayDTO> report = orderService.getReportFromDayToDay(startDay, endDay);

        if (report.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(report, HttpStatus.OK);
    }


    @GetMapping("/month/{month}-{year}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getReportByMonth(@PathVariable int month, @PathVariable int year) {

        List<ReportYearDTO> reportMonth = orderService.getReportByMonth(month, year);

        if (reportMonth.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportMonth, HttpStatus.OK);
    }

    @GetMapping("/month/current-month")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getReportOfCurrentMonth() {

        List<ReportDTO> reportMonth = orderService.getReportOfCurrentMonth();

        if (reportMonth.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(reportMonth.get(0).getTotalAmount(), HttpStatus.OK);
    }

    @GetMapping("/product/top5")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getTop5Product() {

        List<ReportProductDTO> reportProductDTOS = orderItemService.getTop5Product();

        if (reportProductDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        return new ResponseEntity<>(reportProductDTOS, HttpStatus.OK);
    }
}
