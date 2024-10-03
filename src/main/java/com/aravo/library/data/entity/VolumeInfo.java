package com.aravo.library.data.entity;

import com.aravo.library.data.Persistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VolumeInfo implements Serializable, Persistable {
    private long id;
    private long workId;
    private int volume;
    private String seriesTitle;

    public VolumeInfo(int index, String title) {
        setVolume(index);
        setSeriesTitle(title);
    }
}
