//package com.hororok.monta.controller;
//
//import com.hororok.monta.dto.request.studyRecord.PostTimerRequestDto;
//import com.hororok.monta.service.TimerService;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@AllArgsConstructor
//public class TimerController {
//
//    private final TimerService timerService;
//
//    @PostMapping("/study-timer/start")
//    public ResponseEntity<?> postTimerStart(@RequestBody PostTimerRequestDto requestDto) {
//        return timerService.postTimerStart(requestDto);
//    }
//
//    @PostMapping("/study-timer/end")
//    public ResponseEntity<?> postTimerEnd() {
//        return timerService.postTimerEnd();
//    }
//}
