package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.modal.Asset;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.service.AssetService;
import com.stan.cryptoTrading.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/assets")
public class AssetController {

    private final AssetService assetService;


    private final UserService userService;

    public AssetController(AssetService assetService, UserService userService) {
        this.assetService = assetService;
        this.userService = userService;
    }

    @GetMapping("{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);

        return new ResponseEntity<>(asset, HttpStatus.OK);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByCoinIdAndUserId(@PathVariable String coinId, @RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);
        Asset asset = assetService.getAssetByUserIdAndCoinId(user.getId(), coinId);

        return new ResponseEntity<>(asset, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Asset>> getUserAssets(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);
        List<Asset> userAssets = assetService.getUserAssets(user.getId());

        return new ResponseEntity<>(userAssets, HttpStatus.OK);
    }
}
