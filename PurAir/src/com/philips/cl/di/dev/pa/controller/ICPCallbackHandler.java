package com.philips.cl.di.dev.pa.controller;

/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without 
the written consent of the copyright holder.

Project           : Android Demo ICP App  

File Name         : EventPublisherServiceActivity.java        

Description       : 

Revision History:
Version 1: 
    Date: 02-Aug-2013
    Original author: Haranadh Kaki
    Description: Updated version    
----------------------------------------------------------------------------*/


import java.io.FileOutputStream;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import android.os.Handler;

import com.philips.cl.di.dev.pa.interfaces.ICPEventListener;

import com.philips.icpinterface.CallbackHandler;
import com.philips.icpinterface.DownloadData;
import com.philips.icpinterface.FileDownload;
import com.philips.icpinterface.ICPClient;
import com.philips.icpinterface.data.Commands;
import com.philips.icpinterface.data.Errors;
/**
*This interface defines the callback method of ICP Client.
*These functions must be implemented by the application. 
*/

public class ICPCallbackHandler implements CallbackHandler
{
	
	ICPEventListener listener;
	int cntOffset = 0;
	int fileSize = 0;
	byte mybyte[];
	int index = 0;
	OutputStream out = null; 
	
	/**
	*Callback function for executeCommand.
	*<p>
	 *@param command	command number. Refer to {@link Commands} for details.
	 *@param status	status of the call. Refer to {@link Errors} for details.
	 * on the error values.
	 *@param obj	object to reference.
	*/
	
	
	public void callback(int command, int status, ICPClient obj)
	{
		System.out.println("DEMOAPP: Callback. Command is:" + command + " status is:" + status);

		switch (command)
		{
			case Commands.SIGNON:
				PerformSignOn_CB(status, obj);
				break;

			case Commands.GET_DATETIME:
				GetDateTime_CB(status, obj);
				break;

			case Commands.GET_TIMEZONES:
				GetTimeZones_CB(status, obj);
				break;

			case Commands.GET_SERVICE_URL:
				GetServicePortal_CB(status, obj);
				break;

			case Commands.RESET:
				ClientReset_CB(status, obj);
				break;

			case Commands.FETCH_EVENTS:
				Events_CB(status, obj);
				break;

			case Commands.GET_COMPONENT_DETAILS:
				icpClientGetComponentDetails_CB(status, obj);
				break;

			case Commands.DOWNLOAD_FILE:
				icpClientDownloadFile_CB(status, obj);
				break;

			case Commands.EVENT_NOTIFICATION:
				EventNotification_CB(status, obj);
				break;

			case Commands.DATA_COLLECTION:
				DataCollection_CB(status, obj);
				break;
			
			case Commands.REGISTER_PRODUCT:
			case Commands.UNREGISTER_PRODUCT:
			case Commands.QUERY_REGISTRATION_STATUS:
			case Commands.QUERY_TC_STATUS:
			case Commands.ACCEPT_TERMSANDCONDITIONS:
				Registeration_CB(status, obj);
				break;
				
			case Commands.KEY_PROVISION:
			case Commands.KEY_DEPROVISION:
				Provisioning_CB(status,obj);
				break;
			
			case Commands.SUBSCRIBE_EVENTS:
				EventSubscription_CB(status,obj);
				break;
			
			case Commands.THIRDPARTY_REGISTER_PROTOCOLADDRS:
				ThirdParyNotification_CB(status,obj);
				break;
				
			case Commands.DOWNLOAD_DATA:
				DownloadData_CB(status,obj);
				break;
			
			case Commands.PUBLISH_EVENT:
				PublishEvent_CB(status,obj);
				break;
			case Commands.CANCEL_EVENT:
				CancelEvent_CB(status,obj);
				break;
			case Commands.EVENT_DISTRIBUTION_LIST :
				EventDistributionList_CB(status,obj);
				break;
			case Commands.PAIRING_ADD_RELATIONSHIP:
				PairingAddRelatioship_CB(status,obj);
				break;
			case Commands.PAIRING_GET_RELATIONSHIPS:
				PairingGetRelations_CB(status,obj);
				break;
			case Commands.PAIRING_ADD_PERMISSIONS:
				PairingAddPermissions_CB(status,obj);
				break;
			case Commands.PAIRING_REMOVE_PERMISSIONS:
				PairingRemovePermissions_CB(status,obj);
				break;
			case Commands.PAIRING_GET_PERMISSIONS:
				PairingGetPermissions_CB(status,obj);
				break;
			default:
				System.out.println("No callback function found!!!");
				break;
		}
	}
		
		
		public void setHandler(ICPEventListener listener)
		{
			this.listener = listener;
		}
		
		
		public void PerformSignOn_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(1,status,obj) ;
		}
		
		public void GetTimeZones_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(2,status,obj) ;
		}
		
		public void GetDateTime_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(3,status,obj) ;
		}
	
		public void GetServicePortal_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(4,status,obj) ;
		}
		
		public void ClientReset_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(5,status,obj) ;
		}
		
		public void Events_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(6,status,obj) ;
		}
		
		public void icpClientGetComponentDetails_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void icpClientDownloadFile_CB(int status, ICPClient obj)
		{
			updateFileDownloadText(status, (ICPClient)obj);
		}
		
		public void EventNotification_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
	
		public void DataCollection_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void Registeration_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void Provisioning_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void EventSubscription_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void ThirdParyNotification_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void DownloadData_CB(int status, ICPClient obj)
		{
			boolean isComplete = false;
			int currentChunklength = 0;
			byte[] bufferOriginal =((DownloadData)obj).getBuffer().array();
			byte[] buffer = bufferOriginal.clone();
			
			/**Message msg = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("Status", status);
			b.putString("name", "rDCP");
			if(((DownloadData)obj).getIsDownloadComplete() == true)
			{
				isComplete = true;
			}
			currentChunklength = ((DownloadData)obj).getCurrentChunkLength();
			
			b.putByteArray("buffer", buffer);
			b.putBoolean("IsDownloadComplete",isComplete);
			b.putInt("CurrentChunkLength", currentChunklength);
			msg.setData(b);
			msg.obj = obj;
			handler.sendMessage(msg);**/
		}
		
		public void PublishEvent_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void CancelEvent_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void EventDistributionList_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		public void PairingAddRelatioship_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void PairingGetRelations_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void PairingAddPermissions_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		public void PairingRemovePermissions_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;		}
		
		public void PairingGetPermissions_CB(int status, ICPClient obj)
		{
			listener.onICPCallbackEventOccurred(7,status,obj) ;
		}
		
		
		
				
		private void updateFileDownloadText(int status, ICPClient obj) {
			// TODO Auto-generated method stub
			int iNumbytes = 0;
			FileDownload fileDownload = (FileDownload)obj;
			fileSize = fileDownload.getFileSize();
							
			System.out.println("\n Chunk Offset Before receieving : " + cntOffset);
			iNumbytes = fileDownload.getDownloadedChunkOffset() - cntOffset;
			cntOffset = fileDownload.getDownloadedChunkOffset();

			System.out.println("\n Downloaded bytes: " + iNumbytes);
			
			if (cntOffset <= fileSize) // Total size of the file to be downloaded
			{
//				int size = Math.min(fileDownload.getDownloadedChunkOffset(), cntOffset);
				
				ByteBuffer buffer = fileDownload.getBuffer();
				mybyte = new byte[fileSize];
				for (int i = 0; i < iNumbytes; i++)
				{
					mybyte[index++] = buffer.get(i);
				}
			}
			else
			{
				cntOffset = 0;
			}
			
			if(index == fileSize)
			{
				System.out.println("\n Download successfully completed! \n");
				try {
					out = new FileOutputStream("/sdcard/UpgradeFile.ZIP", true);
					out.write(mybyte);
					out.close();
				} catch (IOException e) {
					 //TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				listener.onICPCallbackEventOccurred(7,status,obj) ;
			}
		}


		@Override
		public void setHandler(Handler handler) {
			// TODO Auto-generated method stub
			
		}
}