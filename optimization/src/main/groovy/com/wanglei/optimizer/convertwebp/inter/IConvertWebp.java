package com.wanglei.optimizer.convertwebp.inter;

import org.gradle.api.Project;

import java.io.File;
import java.util.List;

public interface IConvertWebp {

    void convertWebp(String webPTool, Project project, File file, List<File> convertFailedList, int convertWebpQuality);
}
