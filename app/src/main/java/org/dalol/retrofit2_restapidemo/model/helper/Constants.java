/*
 * Copyright (c) 2015-2016 Filippo Engidashet. All Rights Reserved.
 * <p>
 *  Save to the extent permitted by law, you may not use, copy, modify,
 *  distribute or create derivative works of this material or any part
 *  of it without the prior written consent of Filippo Engidashet.
 *  <p>
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 */

package org.dalol.retrofit2_restapidemo.model.helper;

/**
 * @author Filippo Engidashet
 * @version 1.0
 * @date today
 */
public class Constants {

    public static final class HTTP {
        public static final String BASE_URL = "http://services.hanselandpetal.com";
    }

    public static final class DATABASE {

        public static final String DB_NAME = "flowers";
        public static final int DB_VERSION = 1;
        public static final String TABLE_NAME = "flower";

        public static final String DROP_QUERY = "DROP TABLE IF EXIST " + TABLE_NAME;

        public static final String GET_FLOWERS_QUERY = "SELECT * FROM " + TABLE_NAME;

        public static final String PRODUCT_ID = "productId";
        public static final String CATEGORY = "category";
        public static final String PRICE = "price";
        public static final String INSTRUCTIONS = "instructions";
        public static final String NAME = "name";
        public static final String PHOTO_URL = "photo_url";
        public static final String PHOTO = "photo";


        public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "" +
                "(" + PRODUCT_ID + " INTEGER PRIMARY KEY not null," +
                CATEGORY + " TEXT not null," +
                PRICE + " TEXT not null," +
                INSTRUCTIONS + " TEXT not null," +
                NAME + " TEXT not null," +
                PHOTO_URL + " TEXT not null," +
                PHOTO + " blob not null)";
    }

    public static final class REFERENCE {
        public static final String FLOWER = Config.PACKAGE_NAME + "flower";
    }

    public static final class Config {
        public static final String PACKAGE_NAME = "org.dalol.retrofit2_restapidemo.";
    }


}
