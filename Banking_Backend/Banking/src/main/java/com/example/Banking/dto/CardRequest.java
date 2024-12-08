package com.example.Banking.dto;

import com.example.Banking.entity.Card;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class CardRequest  {
    private String userId;
    private Card.CardType cardType;
    private Double cardLimit;
    private Double newLimit;

}

