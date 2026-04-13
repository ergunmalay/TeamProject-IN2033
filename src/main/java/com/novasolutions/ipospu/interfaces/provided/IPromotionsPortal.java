package com.novasolutions.ipospu.interfaces.provided;

import com.novasolutions.ipospu.model.PromotionCampaign;

import java.time.LocalDate;
import java.util.List;

public interface IPromotionsPortal {

    PromotionCampaign createCampaign(String name, LocalDate startDate, LocalDate endDate,
                                     double discountPercent, List<Integer> productIds);

    PromotionCampaign updateCampaign(long campaignId, String name, LocalDate startDate, LocalDate endDate,
                                     double discountPercent, List<Integer> productIds);

    PromotionCampaign deactivateCampaign(long campaignId);

    List<PromotionCampaign> getActivePromotions();

    List<PromotionCampaign> getAllCampaigns();

    double calculatePromotionDiscount(List<com.novasolutions.ipospu.model.CartItem> items);
}
