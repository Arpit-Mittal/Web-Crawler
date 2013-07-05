package com.TextScraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class TextCrawl {

	static Result[] rs=new Result[30];
	/*
	 * Retrieve content from a User Specified URL
	 */
	public static String RetrieveContent(String url) {
		HttpURLConnection connection = null;
		BufferedReader rd = null;
		StringBuilder sb = null;
		String line = null;
		String jsp = null;
		URL serverAddress = null;

		try {
			serverAddress = new URL(url);
			/* set up out communications stuff */
			connection = null;

			/* Set up the initial connection */
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			/* establish connection */
			connection.connect();

			/* read the result from the server */
			rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			sb = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				if (!line.trim().equals("")) {
					sb.append(line + '\n');
				}
			}

			jsp = sb.toString().trim();

			return jsp;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("MalformedURLException!");
			return null;
		} catch (ProtocolException e) {
			e.printStackTrace();
			System.out.println("ProtocolException!");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException!");
			return null;
		} finally {
			/* close the connection, set all objects to null */
			connection.disconnect();
			rd = null;
			sb = null;
			connection = null;

		}
	}

	/*
	 * Retrieve Product count corresponding to particular Product Name provided
	 * that all Link and Product Information has to be stored in Database. I
	 * have tested 3 Products and for these 3 Products it is working fine and it
	 * is generalized for all products provided database is there.
	 */

	static int num_results = 0;

	public static int totalResultCount(String productName) {
		String[] splitProductCount;
		String productCount;
		String url = null, jsp;
		if (productName.equalsIgnoreCase("Canon DSLR Cameras")) {
			url = "http://www.sears.com/cameras-camcorders-digital-cameras-digital-slr-cameras&Canon/s-1231481378?filter=Brand&keywordSearch=false&previousSort=ORIGINAL_SORT_ORDER&viewItems=50";

		} else if (productName.equalsIgnoreCase("Nikon DSLR Cameras")) {
			url = "http://www.sears.com/cameras-camcorders-digital-cameras-digital-slr-cameras&Nikon/s-1231481378?filter=Brand&keywordSearch=false&previousSort=ORIGINAL_SORT_ORDER&viewItems=50";
		} else if (productName.equalsIgnoreCase("DSLR Camera Bundles")) {
			url = "http://www.sears.com/cameras-camcorders-digital-cameras-digital-slr-camera-bundles&Sears/s-1231481382?filter=storeOrigin&keyword=camera+bundle&autoRedirect=false&keywordSearch=false&previousSort=ORIGINAL_SORT_ORDER&viewItems=50";
		}

		jsp = RetrieveContent(url);
		/* failure */
		if (jsp == null) {
			System.out.println("Failed to retrieve web page.");
			return 0;
		}

		splitProductCount = jsp.split("productCount = \"");
		productCount = splitProductCount[1].substring(0,
				splitProductCount[1].indexOf("\""));
		System.out
				.println("Total Number of Results(Total Products including all pages) "
						+ productName + ":" + productCount);

		return num_results;
	}

	/*
	 * Print Title/Product Name,Price of Product,Vendor of particular product by
	 * searching product name and particular pageNumber.
	 */
	public static void printProductDetails(String productName, int pageNumber) {
		String[] splitTitle;
		String[] splitPrice;
		String[] splitVendor;
		String priceVendorUrl;
		String domain = null;
		String title = null;
		String url = null, jsp, jspPriceVendor;
		int titleCount = 0;
		int lastCharacterTitle = 0;
		ArrayList<String> titleOfProduct = new ArrayList<String>();
		if (productName.equalsIgnoreCase("Canon DSLR Cameras")) {
			if (pageNumber == 1) {
				url = "http://www.sears.com/cameras-camcorders-digital-cameras-digital-slr-cameras&Canon/s-1231481378?filter=Brand&keywordSearch=false&previousSort=ORIGINAL_SORT_ORDER&viewItems=25";
			} else {
				url = "http://www.sears.com/cameras-camcorders-digital-cameras-digital-slr-cameras&Canon/s-1231481378?filter=Brand&keywordSearch=false&previousSort=ORIGINAL_SORT_ORDER&pageNum="
						+ pageNumber + "&viewItems=25";

			}
		} else if (productName.equalsIgnoreCase("Nikon DSLR Cameras")) {
			if (pageNumber == 1) {
				url = "http://www.sears.com/cameras-camcorders-digital-cameras-digital-slr-cameras&Nikon/s-1231481378?filter=Brand&keywordSearch=false&previousSort=ORIGINAL_SORT_ORDER&viewItems=25";
			} else {
				url = "http://www.sears.com/cameras-camcorders-digital-cameras-digital-slr-cameras&Nikon/s-1231481378?filter=Brand&keywordSearch=false&previousSort=ORIGINAL_SORT_ORDER&pageNum="
						+ pageNumber + "&viewItems=25";

			}
		}

		jsp = RetrieveContent(url);
		/* failure */
		if (jsp == null) {
			System.out.println("Failed to retrieve web page.");

		}
		// Retrieving Title Logic Given Product Name and Page Number matching

		splitTitle = jsp.split("<img title=");

		for (titleCount = 0; titleCount <= 25; titleCount++) {
			if (titleCount > 0) {
				lastCharacterTitle = splitTitle[titleCount].indexOf("class");
				title = splitTitle[titleCount].substring(0, lastCharacterTitle);
				System.out.println("*********TitleNumber********:-"
						+ titleCount);
				System.out.println("Title" + title);
				titleOfProduct.add(title);
			}
		}
		splitTitle = jsp.split("<img title=");
		// Fetching Price and Vendor
		int count = 0;
		for (titleCount = 0; titleCount < 25; titleCount++) {
			count = count + 1;
			System.out
					.println("********Price and Vendor Information Number *********"
							+ count);
			splitPrice = null;
			domain = "http://www.sears.com/";
			priceVendorUrl = splitTitle[titleCount].substring(
					splitTitle[titleCount].length() - 200,
					splitTitle[titleCount].length());
			priceVendorUrl = priceVendorUrl.substring(
					priceVendorUrl.indexOf("info="),
					priceVendorUrl.indexOf("onclick="));
			domain = domain + priceVendorUrl.substring(7);
			System.out
					.println("Price Vendor Url Information Extracted corresponding to particular Product:-"
							+ domain);
			jspPriceVendor = RetrieveContent(domain);
			/* failure */
			if (jspPriceVendor == null) {
				System.out.println("Failed to retrieve web page.");

			}
			try {
				splitPrice = null;
				splitVendor = null;
				splitPrice = jspPriceVendor.split("itemprop=\"price\">");
				splitVendor = jspPriceVendor.split("sellerName\":");

				if (splitPrice.length == 1) {
					
					System.out
							.println("Price/Vendor information cannot be retreived from URL beacuse page Content is in js format while retrieving it from code");

				}
				if (splitPrice.length > 1) {
					System.out.println("Actual Price:"
							+ splitPrice[1].substring(0,
									splitPrice[1].indexOf("</span>")));
					
					
					if (splitVendor[1]
							.substring(0, splitVendor[1].indexOf(",")).length() > 3)
					{
						System.out.println("Vendor Name:"
								+ splitVendor[1].substring(0,
										splitVendor[1].indexOf(",")));
						
						
					}
				
				
					
					else {
						System.out.println("Vendor Name:" + "Sears");
					}

				}
			} catch (Exception e) {
				System.out.println("Exception" + e);
				
			}
		}

	}

	public static void main(String[] args) {
		/* check parameters */
		if (args.length == 0 || args.length > 2) {
			System.out.println("Please input valid parameters.");
			return;
		}
		if (args.length == 1) {
			System.out.println("Total Result Count for"+args[0]);
			num_results = totalResultCount(args[0]);
		}
		
		if (args.length == 2) {
			try {
				int number = Integer.parseInt(args[1]);
				if (number <= 0) {
					System.out.println("You should give a valid page value.");
				}
				System.out
				      .println("\n **********************25 Titles then 25 Prices then 25 Vendors for "+args[0]+"**************************");
				printProductDetails(args[0], number);
			
			} catch (NumberFormatException nfe){
				System.out.println("You should give an integer value to the second parameter.");
				return ;
			}
		}
		
		

	}
}
