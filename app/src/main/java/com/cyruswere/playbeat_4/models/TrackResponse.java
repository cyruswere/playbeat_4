package com.cyruswere.playbeat_4.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

    @Parcel
    public class TrackResponse {

        @SerializedName("resultCount")
        @Expose
        private Integer resultCount;
        @SerializedName("results")
        @Expose
        private List<Result> results = null;

        /**
         * No args constructor for use in serialization
         *
         */
        public TrackResponse() {
        }

        /**
         *
         * @param resultCount
         * @param results
         */
        public TrackResponse(Integer resultCount, List<Result> results) {
            super();
            this.resultCount = resultCount;
            this.results = results;
        }

        public Integer getResultCount() {
            return resultCount;
        }

        public void setResultCount(Integer resultCount) {
            this.resultCount = resultCount;
        }

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

    }
