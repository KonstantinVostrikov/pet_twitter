package com.vostrikov.pet_twitter.controllers


import com.vostrikov.pet_twitter.dto.Feed
import com.vostrikov.pet_twitter.dto.ResponseResult
import com.vostrikov.pet_twitter.services.FeedService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController()
@RequestMapping("/feed")
class FeedController {

    @Autowired
    FeedService feedService

    @PostMapping("/receive")
    def receive(@RequestBody Feed feed) {
        try {
            def posts = feedService.receive(feed)
            return ResponseEntity.ok().body(new ResponseResult("Feed received", posts))
        } catch (Exception ex) {
            log.error(ex.message)
            ex.printStackTrace()
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }

    @GetMapping("/own")
    def own(@RequestParam("userId") String userId) {
        try {
            def posts = feedService.own(userId)
            return ResponseEntity.ok().body(new ResponseResult("Own posts received", posts))
        } catch (Exception ex) {
            log.error(ex.message)
            ex.printStackTrace()
            return ResponseEntity.internalServerError().body(new ResponseResult(ex.message))
        }
    }
}
