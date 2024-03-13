package com.hororok.monta.service.useItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemUseStrategyFactory {
    private final FoodItemUseStrategy foodItemUseStrategy;
    private final ConsumableItemUseStrategy consumableItemUseStrategy;

    @Autowired
    public ItemUseStrategyFactory(FoodItemUseStrategy foodItemUseStrategy, ConsumableItemUseStrategy consumableItemUseStrategy) {
        this.foodItemUseStrategy = foodItemUseStrategy;
        this.consumableItemUseStrategy = consumableItemUseStrategy;
    }

    public ItemUseStrategy getStrategy(int effectCode) {
        if(effectCode >= 10000 && effectCode < 20000) {
            return foodItemUseStrategy;
        } else if(effectCode >= 20000 && effectCode < 30000) {
            return consumableItemUseStrategy;
        } else {
            return null;
        }
    }
}
