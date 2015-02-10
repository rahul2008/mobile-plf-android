package com.philips.cl.di.dev.pa.outdoorlocations.test;

import java.text.SimpleDateFormat;
import java.util.List;

import junit.framework.TestCase;
import android.annotation.SuppressLint;

import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.dashboard.OutdoorDetailHelper;
import com.philips.cl.di.dev.pa.util.DataParser;
import com.philips.cl.di.dev.pa.util.Utils;

public class OutdoorDetailHelperTest extends TestCase {
	
	
	private float lastDayAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F };

	private float last7dayAQIHistoricArr[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F};

	private float last4weekAQIHistoric[] = { -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F,
			-1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F, -1F };
	
	private String historicResultData;
	private String historicResultData1;
	private OutdoorDetailHelper detailHelper;
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void setUp() throws Exception {
		SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentTimeStampStr = timeStampFormat.format(Utils.getCurrentChineseDate());
		String currentDateStr = dateFormat.format(Utils.getCurrentChineseDate());
		detailHelper = new OutdoorDetailHelper(lastDayAQIHistoricArr, last7dayAQIHistoricArr, last4weekAQIHistoric);
		historicResultData = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"error_code\":0,\"result\":[{\"citynow\":{\"city\":\"北京\",\"AQI\":\"108\",\"quality\":\"轻度污染\",\"date\":\""+currentTimeStampStr+"\"},\"lastTwoWeeks\":{\"1\":{\"city\":\"北京\",\"AQI\":\"235\",\"quality\":\"重度污染\",\"date\":\"2015-01-03\"},\"2\":{\"city\":\"北京\",\"AQI\":\"265\",\"quality\":\"重度污染\",\"date\":\"2015-01-04\"},\"3\":{\"city\":\"北京\",\"AQI\":\"65\",\"quality\":\"良\",\"date\":\"2015-01-05\"},\"4\":{\"city\":\"北京\",\"AQI\":\"87\",\"quality\":\"良\",\"date\":\"2015-01-06\"},\"5\":{\"city\":\"北京\",\"AQI\":\"72\",\"quality\":\"良\",\"date\":\"2015-01-16\"},\"6\":{\"city\":\"北京\",\"AQI\":\"167\",\"quality\":\"中度污染\",\"date\":\"2015-01-17\"},\"7\":{\"city\":\"北京\",\"AQI\":\"110\",\"quality\":\"轻度污染\",\"date\":\"2015-01-18\"},\"8\":{\"city\":\"北京\",\"AQI\":\"164\",\"quality\":\"中度污染\",\"date\":\"2015-01-19\"},\"9\":{\"city\":\"北京\",\"AQI\":\"190\",\"quality\":\"中度污染\",\"date\":\"2015-01-20\"},\"10\":{\"city\":\"北京\",\"AQI\":\"97\",\"quality\":\"良\",\"date\":\"2015-01-21\"},\"11\":{\"city\":\"北京\",\"AQI\":\"221\",\"quality\":\"重度污染\",\"date\":\"2015-01-22\"},\"12\":{\"city\":\"北京\",\"AQI\":\"294\",\"quality\":\"重度污染\",\"date\":\"2015-01-23\"},\"13\":{\"city\":\"北京\",\"AQI\":\"164\",\"quality\":\"中度污染\",\"date\":\"2015-01-24\"},\"14\":{\"city\":\"北京\",\"AQI\":\"240\",\"quality\":\"重度污染\",\"date\":\"2015-01-25\"},\"15\":{\"city\":\"北京\",\"AQI\":\"33\",\"quality\":\"优\",\"date\":\"2015-01-26\"},\"16\":{\"city\":\"北京\",\"AQI\":\"115\",\"quality\":\"轻度污染\",\"date\":\"2015-01-27\"},\"17\":{\"city\":\"北京\",\"AQI\":\"174\",\"quality\":\"中度污染\",\"date\":\"2015-01-28\"},\"18\":{\"city\":\"北京\",\"AQI\":\"55\",\"quality\":\"良\",\"date\":\"2015-01-29\"},\"19\":{\"city\":\"北京\",\"AQI\":\"29\",\"quality\":\"优\",\"date\":\"2015-01-30\"},\"20\":{\"city\":\"北京\",\"AQI\":\"179\",\"quality\":\"中度污染\",\"date\":\"2015-01-31\"},\"21\":{\"city\":\"北京\",\"AQI\":\"194\",\"quality\":\"中度污染\",\"date\":\"2015-02-01\"},\"22\":{\"city\":\"北京\",\"AQI\":\"192\",\"quality\":\"中度污染\",\"date\":\"2015-02-02\"},\"23\":{\"city\":\"北京\",\"AQI\":\"80\",\"quality\":\"良\",\"date\":\"2015-02-03\"},\"24\":{\"city\":\"北京\",\"AQI\":\"42\",\"quality\":\"优\",\"date\":\"2015-02-04\"},\"25\":{\"city\":\"北京\",\"AQI\":\"153\",\"quality\":\"中度污染\",\"date\":\"2015-02-05\"},\"26\":{\"city\":\"北京\",\"AQI\":\"169\",\"quality\":\"中度污染\",\"date\":\"2015-02-06\"},\"27\":{\"city\":\"北京\",\"AQI\":\"29\",\"quality\":\"优\",\"date\":\"2015-02-07\"},\"28\":{\"city\":\"北京\",\"AQI\":\"158\",\"quality\":\"中度污染\",\"date\":\"2015-02-08\"}},\"lastMoniData\":{\"1\":{\"city\":\"万寿西宫\",\"AQI\":\"69\",\"America_AQI\":\"120\",\"quality\":\"良\",\"PM2.5Hour\":\"50 \",\"PM2.5Day\":\"50 \",\"PM10Hour\":\"— \",\"lat\":\"39.878\",\"lon\":\"116.352\"},\"2\":{\"city\":\"定陵\",\"AQI\":\"125\",\"America_AQI\":\"167\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"95 \",\"PM2.5Day\":\"95 \",\"PM10Hour\":\"— \",\"lat\":\"40.292\",\"lon\":\"116.22\"},\"3\":{\"city\":\"东四\",\"AQI\":\"117\",\"America_AQI\":\"163\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"88 \",\"PM2.5Day\":\"88 \",\"PM10Hour\":\"— \",\"lat\":\"39.929\",\"lon\":\"116.417\"},\"4\":{\"city\":\"天坛\",\"AQI\":\"33\",\"America_AQI\":\"13\",\"quality\":\"优\",\"PM2.5Hour\":\"— \",\"PM2.5Day\":\"— \",\"PM10Hour\":\"— \",\"lat\":\"39.886\",\"lon\":\"116.407\"},\"5\":{\"city\":\"农展馆\",\"AQI\":\"114\",\"America_AQI\":\"162\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"86 \",\"PM2.5Day\":\"86 \",\"PM10Hour\":\"— \",\"lat\":\"39.937\",\"lon\":\"116.461\"},\"6\":{\"city\":\"官园\",\"AQI\":\"104\",\"America_AQI\":\"157\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"78 \",\"PM2.5Day\":\"78 \",\"PM10Hour\":\"— \",\"lat\":\"39.929\",\"lon\":\"116.339\"},\"7\":{\"city\":\"海淀区万柳\",\"AQI\":\"113\",\"America_AQI\":\"161\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"85 \",\"PM2.5Day\":\"85 \",\"PM10Hour\":\"— \",\"lat\":\"\",\"lon\":\"\"},\"8\":{\"city\":\"顺义新城\",\"AQI\":\"115\",\"America_AQI\":\"162\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"87 \",\"PM2.5Day\":\"87 \",\"PM10Hour\":\"— \",\"lat\":\"\",\"lon\":\"\"},\"9\":{\"city\":\"怀柔镇\",\"AQI\":\"80\",\"America_AQI\":\"138\",\"quality\":\"良\",\"PM2.5Hour\":\"59 \",\"PM2.5Day\":\"59 \",\"PM10Hour\":\"77 \",\"lat\":\"\",\"lon\":\"\"},\"10\":{\"city\":\"昌平镇\",\"AQI\":\"144\",\"America_AQI\":\"176\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"110 \",\"PM2.5Day\":\"110 \",\"PM10Hour\":\"118 \",\"lat\":\"\",\"lon\":\"\"},\"11\":{\"city\":\"奥体中心\",\"AQI\":\"100\",\"America_AQI\":\"155\",\"quality\":\"良\",\"PM2.5Hour\":\"75 \",\"PM2.5Day\":\"75 \",\"PM10Hour\":\"80 \",\"lat\":\"39.982\",\"lon\":\"116.397\"},\"12\":{\"city\":\"古城\",\"AQI\":\"114\",\"America_AQI\":\"162\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"86 \",\"PM2.5Day\":\"86 \",\"PM10Hour\":\"91 \",\"lat\":\"39.914\",\"lon\":\"116.184\"},\"13\":{\"city\":\"美国大使馆\",\"AQI\":\"160\",\"America_AQI\":\"165\",\"quality\":\"中度污染\",\"PM2.5Hour\":\"82 \",\"PM2.5Day\":\"82 \",\"PM10Hour\":\"— \",\"lat\":\"\",\"lon\":\"\"}}}]}";
		historicResultData1 = "{\"resultcode\":\"200\",\"reason\":\"SUCCESSED!\",\"error_code\":0,\"result\":[{\"citynow\":{\"city\":\"北京\",\"AQI\":\"108\",\"quality\":\"轻度污染\",\"date\":\""+currentTimeStampStr+"\"},\"lastTwoWeeks\":{\"1\":{\"city\":\"北京\",\"AQI\":\"235\",\"quality\":\"重度污染\",\"date\":\"2015-01-03\"},\"2\":{\"city\":\"北京\",\"AQI\":\"265\",\"quality\":\"重度污染\",\"date\":\"2015-01-04\"},\"3\":{\"city\":\"北京\",\"AQI\":\"65\",\"quality\":\"良\",\"date\":\"2015-01-05\"},\"4\":{\"city\":\"北京\",\"AQI\":\"87\",\"quality\":\"良\",\"date\":\"2015-01-06\"},\"5\":{\"city\":\"北京\",\"AQI\":\"72\",\"quality\":\"良\",\"date\":\"2015-01-16\"},\"6\":{\"city\":\"北京\",\"AQI\":\"167\",\"quality\":\"中度污染\",\"date\":\"2015-01-17\"},\"7\":{\"city\":\"北京\",\"AQI\":\"110\",\"quality\":\"轻度污染\",\"date\":\"2015-01-18\"},\"8\":{\"city\":\"北京\",\"AQI\":\"164\",\"quality\":\"中度污染\",\"date\":\"2015-01-19\"},\"9\":{\"city\":\"北京\",\"AQI\":\"190\",\"quality\":\"中度污染\",\"date\":\"2015-01-20\"},\"10\":{\"city\":\"北京\",\"AQI\":\"97\",\"quality\":\"良\",\"date\":\"2015-01-21\"},\"11\":{\"city\":\"北京\",\"AQI\":\"221\",\"quality\":\"重度污染\",\"date\":\"2015-01-22\"},\"12\":{\"city\":\"北京\",\"AQI\":\"294\",\"quality\":\"重度污染\",\"date\":\"2015-01-23\"},\"13\":{\"city\":\"北京\",\"AQI\":\"164\",\"quality\":\"中度污染\",\"date\":\"2015-01-24\"},\"14\":{\"city\":\"北京\",\"AQI\":\"240\",\"quality\":\"重度污染\",\"date\":\"2015-01-25\"},\"15\":{\"city\":\"北京\",\"AQI\":\"33\",\"quality\":\"优\",\"date\":\"2015-01-26\"},\"16\":{\"city\":\"北京\",\"AQI\":\"115\",\"quality\":\"轻度污染\",\"date\":\"2015-01-27\"},\"17\":{\"city\":\"北京\",\"AQI\":\"174\",\"quality\":\"中度污染\",\"date\":\"2015-01-28\"},\"18\":{\"city\":\"北京\",\"AQI\":\"55\",\"quality\":\"良\",\"date\":\"2015-01-29\"},\"19\":{\"city\":\"北京\",\"AQI\":\"29\",\"quality\":\"优\",\"date\":\"2015-01-30\"},\"20\":{\"city\":\"北京\",\"AQI\":\"179\",\"quality\":\"中度污染\",\"date\":\"2015-01-31\"},\"21\":{\"city\":\"北京\",\"AQI\":\"194\",\"quality\":\"中度污染\",\"date\":\"2015-02-01\"},\"22\":{\"city\":\"北京\",\"AQI\":\"192\",\"quality\":\"中度污染\",\"date\":\"2015-02-02\"},\"23\":{\"city\":\"北京\",\"AQI\":\"80\",\"quality\":\"良\",\"date\":\"2015-02-03\"},\"24\":{\"city\":\"北京\",\"AQI\":\"42\",\"quality\":\"优\",\"date\":\"2015-02-04\"},\"25\":{\"city\":\"北京\",\"AQI\":\"153\",\"quality\":\"中度污染\",\"date\":\"2015-02-05\"},\"26\":{\"city\":\"北京\",\"AQI\":\"169\",\"quality\":\"中度污染\",\"date\":\"2015-02-06\"},\"27\":{\"city\":\"北京\",\"AQI\":\"29\",\"quality\":\"优\",\"date\":\"2015-02-07\"},\"28\":{\"city\":\"北京\",\"AQI\":\"158\",\"quality\":\"中度污染\",\"date\":\""+currentDateStr+"\"}},\"lastMoniData\":{\"1\":{\"city\":\"万寿西宫\",\"AQI\":\"69\",\"America_AQI\":\"120\",\"quality\":\"良\",\"PM2.5Hour\":\"50 \",\"PM2.5Day\":\"50 \",\"PM10Hour\":\"— \",\"lat\":\"39.878\",\"lon\":\"116.352\"},\"2\":{\"city\":\"定陵\",\"AQI\":\"125\",\"America_AQI\":\"167\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"95 \",\"PM2.5Day\":\"95 \",\"PM10Hour\":\"— \",\"lat\":\"40.292\",\"lon\":\"116.22\"},\"3\":{\"city\":\"东四\",\"AQI\":\"117\",\"America_AQI\":\"163\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"88 \",\"PM2.5Day\":\"88 \",\"PM10Hour\":\"— \",\"lat\":\"39.929\",\"lon\":\"116.417\"},\"4\":{\"city\":\"天坛\",\"AQI\":\"33\",\"America_AQI\":\"13\",\"quality\":\"优\",\"PM2.5Hour\":\"— \",\"PM2.5Day\":\"— \",\"PM10Hour\":\"— \",\"lat\":\"39.886\",\"lon\":\"116.407\"},\"5\":{\"city\":\"农展馆\",\"AQI\":\"114\",\"America_AQI\":\"162\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"86 \",\"PM2.5Day\":\"86 \",\"PM10Hour\":\"— \",\"lat\":\"39.937\",\"lon\":\"116.461\"},\"6\":{\"city\":\"官园\",\"AQI\":\"104\",\"America_AQI\":\"157\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"78 \",\"PM2.5Day\":\"78 \",\"PM10Hour\":\"— \",\"lat\":\"39.929\",\"lon\":\"116.339\"},\"7\":{\"city\":\"海淀区万柳\",\"AQI\":\"113\",\"America_AQI\":\"161\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"85 \",\"PM2.5Day\":\"85 \",\"PM10Hour\":\"— \",\"lat\":\"\",\"lon\":\"\"},\"8\":{\"city\":\"顺义新城\",\"AQI\":\"115\",\"America_AQI\":\"162\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"87 \",\"PM2.5Day\":\"87 \",\"PM10Hour\":\"— \",\"lat\":\"\",\"lon\":\"\"},\"9\":{\"city\":\"怀柔镇\",\"AQI\":\"80\",\"America_AQI\":\"138\",\"quality\":\"良\",\"PM2.5Hour\":\"59 \",\"PM2.5Day\":\"59 \",\"PM10Hour\":\"77 \",\"lat\":\"\",\"lon\":\"\"},\"10\":{\"city\":\"昌平镇\",\"AQI\":\"144\",\"America_AQI\":\"176\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"110 \",\"PM2.5Day\":\"110 \",\"PM10Hour\":\"118 \",\"lat\":\"\",\"lon\":\"\"},\"11\":{\"city\":\"奥体中心\",\"AQI\":\"100\",\"America_AQI\":\"155\",\"quality\":\"良\",\"PM2.5Hour\":\"75 \",\"PM2.5Day\":\"75 \",\"PM10Hour\":\"80 \",\"lat\":\"39.982\",\"lon\":\"116.397\"},\"12\":{\"city\":\"古城\",\"AQI\":\"114\",\"America_AQI\":\"162\",\"quality\":\"轻度污染\",\"PM2.5Hour\":\"86 \",\"PM2.5Day\":\"86 \",\"PM10Hour\":\"91 \",\"lat\":\"39.914\",\"lon\":\"116.184\"},\"13\":{\"city\":\"美国大使馆\",\"AQI\":\"160\",\"America_AQI\":\"165\",\"quality\":\"中度污染\",\"PM2.5Hour\":\"82 \",\"PM2.5Day\":\"82 \",\"PM10Hour\":\"— \",\"lat\":\"\",\"lon\":\"\"}}}]}";
		super.setUp();
	}
	
	public void testUSEmbassyHistoricAQILastDayData() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		lastDayAQIHistoricArr = detailHelper.getUpdateLastDayAQIHistoricArr();
		assertEquals(108F, lastDayAQIHistoricArr[lastDayAQIHistoricArr.length - 1], 0F);
	}
	
	public void testUSEmbassyHistoricAQILastDayDataFalse() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		lastDayAQIHistoricArr = detailHelper.getUpdateLastDayAQIHistoricArr();
		assertFalse(108F > lastDayAQIHistoricArr[lastDayAQIHistoricArr.length - 1]);
		assertFalse(108F < lastDayAQIHistoricArr[lastDayAQIHistoricArr.length - 1]);
	}
	
	public void testUSEmbassyHistoricAQILast7DayDataWithoutCurrentDayData() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last7dayAQIHistoricArr = detailHelper.getUpdateLast7DayAQIHistoricArr();
		assertEquals(108F, last7dayAQIHistoricArr[last7dayAQIHistoricArr.length - 1], 0F);
		last4weekAQIHistoric = detailHelper.getUpdateLast4weekAQIHistoricArr();
		System.out.println("outdoorAQIs" + outdoorAQIs);
	}
	
	public void testUSEmbassyHistoricAQILast7DayDataWithoutCurrentDayDataFalse() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last7dayAQIHistoricArr = detailHelper.getUpdateLast7DayAQIHistoricArr();
		assertFalse(108F > last7dayAQIHistoricArr[last7dayAQIHistoricArr.length - 1]);
		assertFalse(108F < last7dayAQIHistoricArr[last7dayAQIHistoricArr.length - 1]);
		last4weekAQIHistoric = detailHelper.getUpdateLast4weekAQIHistoricArr();
		System.out.println("outdoorAQIs" + outdoorAQIs);
	}
	
	public void testUSEmbassyHistoricAQILast4weekDataWithoutCurrentDayData() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last4weekAQIHistoric = detailHelper.getUpdateLast4weekAQIHistoricArr();
		assertEquals(108F, last4weekAQIHistoric[last4weekAQIHistoric.length - 1], 0F);
	}
	
	public void testUSEmbassyHistoricAQILast4weekDataWithoutCurrentDayDataFalse() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last4weekAQIHistoric = detailHelper.getUpdateLast4weekAQIHistoricArr();
		assertFalse(108F > last4weekAQIHistoric[last4weekAQIHistoric.length - 1]);
		assertFalse(108F < last4weekAQIHistoric[last4weekAQIHistoric.length - 1]);
	}

	public void testUSEmbassyHistoricAQILast7DayDataWithCurrentDayData() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData1);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last7dayAQIHistoricArr = detailHelper.getUpdateLast7DayAQIHistoricArr();
		assertEquals(158F, last7dayAQIHistoricArr[last7dayAQIHistoricArr.length - 1], 0F);
	}
	
	public void testUSEmbassyHistoricAQILast7DayDataWithCurrentDayDataFalse() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData1);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last7dayAQIHistoricArr = detailHelper.getUpdateLast7DayAQIHistoricArr();
		assertFalse(108F == last4weekAQIHistoric[last4weekAQIHistoric.length - 1]);
	}
	
	public void testUSEmbassyHistoricAQILast4weekDataWithCurrentDayData() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData1);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last4weekAQIHistoric = detailHelper.getUpdateLast7DayAQIHistoricArr();
		assertEquals(158F, last4weekAQIHistoric[last4weekAQIHistoric.length - 1], 0F);
	}
	
	public void testUSEmbassyHistoricAQILast4weekDataWithCurrentDayDataFalse() {
		List<OutdoorAQI> outdoorAQIs = DataParser.parseUSEmbassyHistoricalAQIData(historicResultData1);
		detailHelper.calculateUSEmbassyAQIHistoricData(outdoorAQIs);
		last4weekAQIHistoric = detailHelper.getUpdateLast7DayAQIHistoricArr();
		assertFalse(108F == last4weekAQIHistoric[last4weekAQIHistoric.length - 1]);
	}

}
