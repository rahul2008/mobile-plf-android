package com.philips.cdp.di.ecs.ProductCatalog;

public class ProductListTest {



    String getProductListJSON(){

        return
                "{\n" +
                        "  \"type\": \"productCategorySearchPageWsDTO\",\n" +
                        "  \"currentQuery\": {\n" +
                        "    \"query\": {\n" +
                        "      \"value\": \":relevance:category:Tuscany_Campaign\"\n" +
                        "    },\n" +
                        "    \"url\": \"/search?q=%3Arelevance%3Acategory%3ATuscany_Campaign\"\n" +
                        "  },\n" +
                        "  \"freeTextSearch\": \"\",\n" +
                        "  \"pagination\": {\n" +
                        "    \"currentPage\": 0,\n" +
                        "    \"pageSize\": 20,\n" +
                        "    \"sort\": \"relevance\",\n" +
                        "    \"totalPages\": 1,\n" +
                        "    \"totalResults\": 18\n" +
                        "  },\n" +
                        "  \"products\": [\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX9033/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX9033_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX9023/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX9023_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX6042/94\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$21.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 21.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$21.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 21.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX6042_94\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX9003/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$38.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 38.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$38.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 38.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX9003_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX8332/11\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$89.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 89.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$89.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 89.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX8332_11\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX9043/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$38.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 38.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$38.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 38.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX9043_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX6062/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX6062_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"DIS251/01\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$4.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 4.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$4.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 4.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/DIS251_01\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX9042/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX9042_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX6063/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$38.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 38.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$38.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 38.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX6063_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"DIS364/03\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$9.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 9.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$9.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 9.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/DIS364_03\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"DIS359/03\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$14.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 14.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$14.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 14.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/DIS359_03\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX6032/94\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$21.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 21.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$21.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 21.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX6032_94\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"DIS363/03\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$9.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 9.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$9.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 9.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/DIS363_03\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX9002/64\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$29.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 29.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX9002_64\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX8071/10\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$14.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 14.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$14.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 14.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX8071_10\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX6321/02\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$49.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 49.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$49.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 49.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevelStatus\": \"inStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX6321_02\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"availableForPickup\": false,\n" +
                        "      \"code\": \"HX8332/12\",\n" +
                        "      \"discountPrice\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$89.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 89.99\n" +
                        "      },\n" +
                        "      \"price\": {\n" +
                        "        \"currencyIso\": \"USD\",\n" +
                        "        \"formattedValue\": \"$89.99\",\n" +
                        "        \"priceType\": \"BUY\",\n" +
                        "        \"value\": 89.99\n" +
                        "      },\n" +
                        "      \"priceRange\": {},\n" +
                        "      \"stock\": {\n" +
                        "        \"stockLevel\": 0,\n" +
                        "        \"stockLevelStatus\": \"outOfStock\"\n" +
                        "      },\n" +
                        "      \"url\": \"/c//p/HX8332_12\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"sorts\": [\n" +
                        "    {\n" +
                        "      \"code\": \"relevance\",\n" +
                        "      \"selected\": true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"code\": \"topRated\",\n" +
                        "      \"selected\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"code\": \"name-asc\",\n" +
                        "      \"selected\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"code\": \"name-desc\",\n" +
                        "      \"selected\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"code\": \"price-asc\",\n" +
                        "      \"selected\": false\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"code\": \"price-desc\",\n" +
                        "      \"selected\": false\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";



    }
}
