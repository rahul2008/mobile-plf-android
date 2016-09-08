using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Exchange.WebServices.Data;

namespace Philips.H2H.Foundation.AutomationCore.MailServiceUtility
{
    // This sample is for demonstration purposes only. Before you run this sample, make sure that the code meets the coding requirements of your organization.
    public class ExchangeServiceUtility
    {
        static ExchangeService service = Service.ConnectToService(UserConfigData.GetUserData());

        public Email GetUnReadMessageBodyText(string folderName)
        {
            // Attempt to retrieve the unique identifier of the folder with display name "Custom Folder" (located in the Inbox folder).
            FolderId folderId = FindFolderIdByDisplayName(service, folderName, WellKnownFolderName.Inbox);

            if (folderId != null)
            {
                // The search filter to get unread email.
                SearchFilter sf = new SearchFilter.SearchFilterCollection
                    (LogicalOperator.And, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));
                
                ItemView view = new ItemView(1);
                PropertySet itempropertyset = new PropertySet(BasePropertySet.FirstClassProperties);
                itempropertyset.RequestedBodyType = BodyType.Text;
                view.PropertySet = itempropertyset;
                view.OrderBy.Add(EmailMessageSchema.DateTimeReceived, SortDirection.Descending);
                                
                FindItemsResults<Item> findResults = service.FindItems(folderId, view);
                service.LoadPropertiesForItems(findResults.Items, itempropertyset);

                EmailMessage lastMessage = (EmailMessage)findResults.Items[0];

                return new Email
                {
                    MessageText = lastMessage.Body.Text,
                    MessageSubject = lastMessage.Subject
                };
            }
            else
            {
                Logger.Debug("The folder " + folderName + " was not found in the Inbox folder.");
                return null;
            }
        }

        public static FolderId FindFolderIdByDisplayName(ExchangeService service, string DisplayName, WellKnownFolderName SearchFolder)
        {
            // Specify the root folder to be searched.
            Folder rootFolder = Folder.Bind(service, SearchFolder);

            // Loop through the child folders of the folder being searched.
            foreach (Folder folder in rootFolder.FindFolders(new FolderView(100)))
            {
                // If the display name of the current folder matches the specified display name, return the folder's unique identifier.
                if (folder.DisplayName == DisplayName)
                {
                    return folder.Id;
                }
            }

            // If no folders have a display name that matches the specified display name, return null.
            return null;
        }

        public void SendEmail(Email msg, string to)
        {
            string[] sMailList = to.Split(',');
            // Create an email message and identify the Exchange service.
            EmailMessage message = new EmailMessage(service);
            // Add properties to the email message.
            message.Subject = msg.MessageSubject;
            message.Body = msg.MessageText;
            message.Body.BodyType = BodyType.HTML;

            foreach (var item in sMailList)
            {
                message.ToRecipients.Add(item);
            }
            // Send the email message and do not save a copy.
            message.Send();
        }
    }

    public class Email
    {
        public string MessageText { get; set; }
        public string MessageSubject { get; set; }
    }
}
