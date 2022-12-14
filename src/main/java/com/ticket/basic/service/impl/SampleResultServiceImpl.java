package com.ticket.basic.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ticket.basic.mapper.SampleResultInfoMapper;
import com.ticket.basic.domain.entity.SampleResultInfo;
import com.ticket.basic.service.SampleResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SampleResultServiceImpl implements SampleResultService{
    private static final Logger log = LoggerFactory.getLogger(SampleResultServiceImpl.class);
    @Autowired
    private SampleResultInfoMapper sampleMapper;

    @Override
    public SampleResultInfo getSampleResultInfo(Integer id) {
        SampleResultInfo sampleResultInfo = sampleMapper.getSampleResultInfo(id);
        if (Objects.nonNull(sampleResultInfo))
            return sampleResultInfo;
        else
            return null;
    }

    @Override
    public Map<String, List<List<Number>>> getSampleResultDataListByResultId(Integer resultId, String dataType) {
        List<SampleResultInfo> sampleResultInfoList = sampleMapper.getSampleResultInfoListByResultId(resultId);

        Map<String, List<List<Number>>> sampleResultContainer = new HashMap<>();
        if (Objects.nonNull(sampleResultInfoList)) {
            for (SampleResultInfo resultInfo : sampleResultInfoList) {
                String sampleLabel = resultInfo.getSamplerLabel();
                List<List<Number>> series = null;
                List<Number> seriesElement = null;
                if (Objects.isNull(sampleResultContainer.get(sampleLabel))) {
                    series = new ArrayList<>();
                    seriesElement = this.createSeriesElement(resultInfo, dataType);
                    series.add(seriesElement);
                    sampleResultContainer.put(sampleLabel, series);
                } else {
                    seriesElement = this.createSeriesElement(resultInfo, dataType);
                    sampleResultContainer.get(sampleLabel).add(seriesElement);
                }
            }
        }
        return sampleResultContainer;
    }

    private List<Number> createSeriesElement(SampleResultInfo resultInfo, String dataType) {
        List<Number> seriesElement = new ArrayList<>();
        Long timestamp = resultInfo.getTimeStamp().getTime();
        seriesElement.add(timestamp);
        if (Objects.equals(dataType, "meanTime"))
            seriesElement.add(resultInfo.getMeanTime());
        if (Objects.equals(dataType, "requestRate"))
            seriesElement.add(resultInfo.getRequestRate());
        if (Objects.equals(dataType, "errorPercentage"))
            seriesElement.add(resultInfo.getErrorPercentage());
        if (Objects.equals(dataType, "threadCount"))
            seriesElement.add(resultInfo.getThreadCount());
        if (Objects.equals(dataType, "receiveKBPerSecond"))
            seriesElement.add(resultInfo.getReceiveKBPerSecond());
        if (Objects.equals(dataType, "sentKBPerSecond"))
            seriesElement.add(resultInfo.getSentKBPerSecond());
        return seriesElement;
    }

    /**
     * @description                     ??????resultId???????????????SampleResultList,??????????????????????????????.
     * @param resultId                  ????????????ID
     * @return                          ????????????????????????
     */
    @Override
    public List<SampleResultInfo> getSampleResultData(Integer resultId) {
        List<String> samplerLabelList = sampleMapper.getSamplerLabelByResultId(resultId);
        List<SampleResultInfo> sampleResultData = new ArrayList<>();
        for (String samplerLabel: samplerLabelList) {
            SampleResultInfo resultInfo = sampleMapper.getSampleResultData(resultId, samplerLabel);
            sampleResultData.add(resultInfo);
        }
        return sampleResultData;
    }

    /**
     * @description                     ?????????SampleResult?????????DB???
     * @param runningResultId           ????????????ID
     * @param runningSampleResultList   ???Redis????????????SampleResultList
     */
    @Override
    public void addSampleResultToDB(Integer runningResultId, BoundListOperations<String, String> runningSampleResultList) {
        List<SampleResultInfo> sampleResultInfoList = new ArrayList<>();
        // ???Redis????????????SampleResultList????????????
        while (runningSampleResultList.size() > 0) {
            // ???Redis???????????????SampleResult????????????
            String sampleResultString = runningSampleResultList.leftPop();
            // ????????????JSON??????????????????SampleResult??????
            SampleResultInfo sampleResultInfo = JSONObject.parseObject(sampleResultString, SampleResultInfo.class);
            sampleResultInfo.setResultId(runningResultId);
            sampleResultInfoList.add(sampleResultInfo);
        }
        // ????????????????????????DB
        sampleMapper.insertSampleResultInfoList(sampleResultInfoList);
    }

    @Override
    public Map<String, Object> delSampleResultInfoByResultId(Integer resultId) {
        Map<String, Object> result = new HashMap<>();
        Integer deleteFlag = sampleMapper.deleteSampleResultInfoByResultId(resultId);
        if (deleteFlag > 0) {
            result.put("status", "Success");
            result.put("message", "????????????????????????");
        } else {
            result.put("status", "Fail");
            result.put("message", "????????????????????????");
        }
        return result;
    }
}
