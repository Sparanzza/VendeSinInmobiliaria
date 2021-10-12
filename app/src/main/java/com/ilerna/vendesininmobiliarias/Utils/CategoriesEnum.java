package com.ilerna.vendesininmobiliarias.Utils;

import com.ilerna.vendesininmobiliarias.R;

public enum CategoriesEnum {
    FLATS(R.drawable.ic_flat),
    HOMES(R.drawable.ic_home),
    FIELDS(R.drawable.ic_field),
    OFFICES(R.drawable.ic_office),
    STORAGES(R.drawable.ic_storageroom),
    GARAGES(R.drawable.ic_garage),
    FACTORIES(R.drawable.ic_factory),
    COMMERCIALS(R.drawable.ic_shop);

    private final int resource;

    CategoriesEnum(int resource) {
        this.resource = resource;
    }

    public int getResource() {
        return this.resource;
    }
}