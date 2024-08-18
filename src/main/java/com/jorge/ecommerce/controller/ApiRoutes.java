package com.jorge.ecommerce.controller;

public final class ApiRoutes {
    private ApiRoutes() {} 

    public static final class V1 {
        private V1() {} 

        private static final String BASE_PATH = "/api/v1";

        public static final class User {
            private User() {}

            public static final String ROOT = BASE_PATH + "/users";
            public static final String GET_CURRENT_USER_INFO = "/profile";
            public static final String GET_BY_ID = "/{userId}";
            public static final String POST_ADD_ROLE = "/{userId}/roles";
            public static final String DELETE_ROLE = "/{userId}/roles/{roleId}";
        }

        public static final class Product {
            private Product() {}

            public static final String ROOT = BASE_PATH + "/products";
            public static final String GET_BY_ID = "/{productId}";
            public static final String PUT_UPDATE = "/{productId}";
        }

        public static final class Order {
            private Order() {}

            public static final String ROOT = BASE_PATH + "/orders";
            public static final String PUT_UPDATE_STATUS = "/{orderId}";
        }

        public static final class Category {
            private Category() {}

            public static final String ROOT = BASE_PATH + "/categories";
            public static final String GET_BY_ID = "/{categoryId}";
            public static final String UPDATE = "/{categoryId}";
        }

        public static final class Cart {
            private Cart() {}

            public static final String ROOT = BASE_PATH + "/carts/items";
            public static final String UPDATE_ITEM_FROM_CART = "/{productId}";
            public static final String REMOVE_ITEM_FROM_CART = "/{productId}";
        }

        public static final class Auth {
            private Auth() {}

            public static final String ROOT = BASE_PATH + "/auth";
            public static final String LOGIN = "/login";
            public static final String REGISTER = "/register";
            public static final String REFRESH_TOKEN = "/refresh-token";
        }

        public static final class AddressLine {
            private AddressLine() {}

            public static final String ROOT = BASE_PATH + "/addressLines";
            public static final String UPDATE = "/{addressLineId}";
            public static final String SET_DEFAULT = "/{addressLineId}/set-default";
        }
    }

    // Example for Version 2
    /*public static final class V2{
        private V2() {}

        private static final String BASE_PATH = "/api/v2";

        public static final class User {
            private User() {}

            public static final String ROOT = BASE_PATH + "/users";
        }
    }*/
}