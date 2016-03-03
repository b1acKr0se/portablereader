package com.framgia.nguyenthanhhai.portablereader.presenter.listing;

import android.util.Xml;

import com.framgia.nguyenthanhhai.portablereader.data.model.FeedItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    public static final String TAG_RSS = "rss";
    public static final String TAG_CHANNEL = "channel";
    public static final String TAG_ITEM = "item";
    public static final String TAG_TITLE = "title";
    public static final String TAG_DESC = "description";
    public static final String TAG_LINK = "link";
    public static final String TAG_PUBDATE = "pubDate";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_ENCLOSURE = "enclosure";
    public static final String NAMESPACE = null;

    public static List<FeedItem> parse(String xml) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(new StringReader(xml));
        parser.nextTag();
        return readFeed(parser);
    }

    private static List<FeedItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<FeedItem> list = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_RSS);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equalsIgnoreCase(TAG_CHANNEL)) {
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals(TAG_ITEM)) {
                        list.add(readItem(parser));
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }
        }
        return list;
    }

    private static FeedItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, TAG_ITEM);
        String title = null;
        String desc = null;
        String link = null;
        String pubDate = null;
        String author = null;
        String image = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            switch (tag) {
                case TAG_TITLE:
                    title = readTitle(parser);
                    break;
                case TAG_DESC:
                    desc = readDesc(parser);
                    break;
                case TAG_LINK:
                    link = readLink(parser);
                    break;
                case TAG_PUBDATE:
                    pubDate = readPubDate(parser);
                    break;
                case TAG_AUTHOR:
                    author = readAuthor(parser);
                    break;
                case TAG_ENCLOSURE:
                    image = readImageLink(parser);
                    break;
                default:
                    skip(parser);
            }
        }
        return new FeedItem.FeedItemBuilder()
                .setTitle(title)
                .setDescription(desc)
                .setLink(link)
                .setPubDate(pubDate)
                .setImage(image)
                .setAuthor(author).build();
    }

    private static String readTitle(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TAG_TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TAG_TITLE);
        return title;
    }

    private static String readDesc(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TAG_DESC);
        String desc = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TAG_DESC);
        return desc;
    }

    private static String readLink(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TAG_LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TAG_LINK);
        return link;
    }

    private static String readPubDate(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TAG_PUBDATE);
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TAG_PUBDATE);
        return pubDate;
    }

    private static String readAuthor(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, TAG_AUTHOR);
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, TAG_AUTHOR);
        return author;
    }

    private static String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static String readImageLink(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_ENCLOSURE);
        String tag = parser.getName();
        if (tag.equals(TAG_ENCLOSURE)) {
            link = parser.getAttributeValue(null, "url");
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, NAMESPACE, TAG_ENCLOSURE);
        return link;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
