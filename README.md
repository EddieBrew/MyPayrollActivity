# MyPayrollActivity
Initial Committ


The MyPayrollActivity is an app that keeps tracks of a Stanford SEP workers shift hours by storing the information
on an AWS Parse Server and a local SQLite database. All data retrieved for query operations are retrieved from 
the local SQLite database, to limit request calls from AWS, thus mimizing the cost for the AWS services. A hidden function is available, which 
allows the uses to download the parse server data to update the local database. 

1) Login Activity--Sign on page to access the AWS database using Parse Server. The user will be able to query the local 
                   the SQLitedatabase, while offline
2) MainActivity---Activity interface where the user's experience interface with 4 tabs using a tabLayout.
                  of several Fragments. The user also have the option os saving his log on information to prevent entering the info every time 
                  at app startup                                                                                                                                                         A) Tab 1: DataEntry - Allows the user to input his daily employment info
                DataEntryFragment Fragment:  User provided inputs containing the SEP workers shift information. A pop-up dialog
                box will appear, allowing the user to approve or make data changes, before sending the info the the server and 
                local database.
                
      B) Tab 2: Query Hours- Provides daily worker Info within a specified date range.                                                                               
                QueryFragmenyFragment: Provides the user the ability to get daily work hour information within a specified date range
                SwipeAdapterFragment: Displays the daily work info using a custom SwipeAdapter class.
                
      C) Tab3: Query Fields- Allows the user to fetch data, depending the desired paramaters selected from the "Select Field" and
               "Select Item" menus
               QueryFieldFragment = Allows the user to select the query fields in which they want return data
               ListViewFragment- List the results from the queries
               
      D) Tab 4: Stanford Axess webpage. Allows user to log into its account to verify data with apps query results


3) StarterApplication---Implement methods to login into AWS Parse Server.
