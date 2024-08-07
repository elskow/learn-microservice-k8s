package learn.microservices.loans.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import learn.microservices.loans.constants.LoanConstants;
import learn.microservices.loans.dto.DevInfoDTO;
import learn.microservices.loans.dto.LoanDTO;
import learn.microservices.loans.dto.ResponseDTO;
import learn.microservices.loans.service.ILoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RefreshScope
public class LoanController {
    private final static Logger logger = LoggerFactory.getLogger(LoanController.class);

    private final ILoanService iLoanService;
    private final Environment environment;
    private final DevInfoDTO devInfoDTO;

    @Value("${build.version}")
    private String buildInfo;

    public LoanController(ILoanService iLoanService, Environment environment, DevInfoDTO devInfoDTO) {
        this.iLoanService = iLoanService;
        this.environment = environment;
        this.devInfoDTO = devInfoDTO;
    }

    @PostMapping("/loans")
    public ResponseEntity<ResponseDTO> createCard(@RequestParam
                                                  @Size(min = 16, max = 16, message = "NIK should have 16 characters")
                                                  String nik) {
        logger.debug("start-create-loan {}", nik);
        iLoanService.createLoan(nik);
        logger.debug("end-create-loan {}", nik);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(LoanConstants.MESSAGE_201, LoanConstants.STATUS_201));
    }

    @GetMapping("/loans")
    public ResponseEntity<LoanDTO> getLoanByNik(
            @RequestParam @Size(min = 16, max = 16, message = "NIK should have 16 characters") String nik) {
        logger.debug("start-fetch-loan {}", nik);
        LoanDTO loanDTO = iLoanService.getLoanByNik(nik);
        logger.debug("end-fetch-loan {}", nik);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loanDTO);
    }

    @PutMapping("/loans")
    public ResponseEntity<ResponseDTO> updateLoan(@RequestBody
                                                  @Valid
                                                  LoanDTO loanDTO) {
        logger.debug("start-update-loan {}", loanDTO.getNik());
        boolean isUpdated = iLoanService.updateLoan(loanDTO);
        logger.debug("end-update-loan {}", loanDTO.getNik());
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_MODIFIED)
                    .body(new ResponseDTO(LoanConstants.STATUS_304, LoanConstants.MESSAGE_304));
        }
    }

    @DeleteMapping("/loans")
    public ResponseEntity<ResponseDTO> deleteLoan(@RequestParam
                                                  @Size(min = 16, max = 16, message = "NIK should have 16 characters")
                                                  String nik) {
        logger.debug("start-delete-loan {}", nik);
        boolean isDeleted = iLoanService.deleteLoan(nik);
        logger.debug("end-delete-loan {}", nik);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_MODIFIED)
                    .body(new ResponseDTO(LoanConstants.STATUS_304, LoanConstants.MESSAGE_304));
        }
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildInfo);
    }


    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("java.version"));
    }

    @GetMapping("/dev-info")
    public ResponseEntity<DevInfoDTO> getDevInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(devInfoDTO);
    }
}
