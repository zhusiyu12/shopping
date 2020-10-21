package com.edu.rest.service;

import com.edu.common.bean.ShoppingResult;

public interface CacheService {
    ShoppingResult syncContenCategory(long categoryId);
}
