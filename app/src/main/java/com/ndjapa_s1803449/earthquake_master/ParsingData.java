package com.ndjapa_s1803449.earthquake_master;

// Author : Ghislain T Ndjapa
// StudentID: S1803449


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ParsingData {

    //static final String urlString = "http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml";
    String rssString = "";

    public String FetchRSS(String urlString) {
        try {

            URL url = new URL(urlString);
            URLConnection conn = (URLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            while ((inputLine = in .readLine()) != null) {
                rssString+=inputLine;
            }

            in .close();

        } catch (Exception e) {

        }
        return rssString;
    }


    public Document getDomElemt (String urlString){

        Document doc = null;
        DocumentBuilderFactory dBf = DocumentBuilderFactory.newInstance();

        try{
            DocumentBuilder db = dBf.newDocumentBuilder();
            StringReader sr = new StringReader(rssString);
            InputSource is = new InputSource(sr);
            //is.setCharacterStream(new StringReader(rssString));
            doc = db.parse(is);

        }catch (ParserConfigurationException e){

            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return doc;
    }

    public String getValue (Element item, String str){

        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem){
        Node child;
        if(elem != null){
            if(elem.hasChildNodes()){
                for(child = elem.getFirstChild(); child != null; child = child.getNextSibling()){
                    if(child.getNodeType()== Node.TEXT_NODE){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }



}
