package com.hororok.monta.service.itemeffects;

import com.hororok.monta.entity.Item;
import com.hororok.monta.service.itemeffects.strategies.CharacterGacha_10000;
import com.hororok.monta.service.itemeffects.strategies.Else;
import com.hororok.monta.service.itemeffects.strategies.StreakGacha_20000;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EffectCodeStrategyFactory {

    private final Map<Integer, EffectCodeStrategy> strategyMap = new HashMap<>();

    @Autowired
    public EffectCodeStrategyFactory(ApplicationContext context) {
        Map<String, EffectCodeStrategy> strategies = context.getBeansOfType(EffectCodeStrategy.class);
        strategies.forEach((name, strategy) -> {
            EffectCode effectCodeAnnotation = strategy.getClass().getAnnotation(EffectCode.class);
            if(effectCodeAnnotation != null) {
                strategyMap.put(effectCodeAnnotation.value(), strategy);
            }
        });
    }

    public EffectCodeStrategy getStrategy(Item item) {
        return strategyMap.getOrDefault(item.getEffectCode(), null);
    }
}
