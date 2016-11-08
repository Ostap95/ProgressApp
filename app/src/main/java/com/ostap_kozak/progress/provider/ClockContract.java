package com.ostap_kozak.progress.provider;

import com.ostap_kozak.progress.BuildConfig;

/**
 * Created by ostapkozak on 04/11/2016.
 */

    /*
    The contract between the clock provider and desk clock. Contains definitions for the supported URIs and data columns.
    ClockContract defines the data model of clock related information.
    This data is stored in a number of table.
     */

public class ClockContract {

    /**
     * This authority is used for writing to or querying from the clock provider.
     */
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    /**
     * This utility cannot be instantiated
     */
    private ClockContract() {};





}
