// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.nntp;

import org.apache.commons.net.io.DotTerminatedMessageWriter;
import java.io.Writer;
import org.apache.commons.net.io.Util;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.Reader;
import org.apache.commons.net.io.DotTerminatedMessageReader;
import org.apache.commons.net.MalformedServerReplyException;
import java.util.StringTokenizer;

public class NNTPClient extends NNTP
{
    private void __parseArticlePointer(final String reply, final ArticlePointer pointer) throws MalformedServerReplyException {
        final StringTokenizer tokenizer = new StringTokenizer(reply);
        if (tokenizer.countTokens() >= 3) {
            tokenizer.nextToken();
            try {
                pointer.articleNumber = Integer.parseInt(tokenizer.nextToken());
            }
            catch (NumberFormatException e) {
                throw new MalformedServerReplyException("Could not parse article pointer.\nServer reply: " + reply);
            }
            pointer.articleId = tokenizer.nextToken();
            return;
        }
        throw new MalformedServerReplyException("Could not parse article pointer.\nServer reply: " + reply);
    }
    
    private void __parseGroupReply(final String reply, final NewsgroupInfo info) throws MalformedServerReplyException {
        final StringTokenizer tokenizer = new StringTokenizer(reply);
        if (tokenizer.countTokens() >= 5) {
            tokenizer.nextToken();
            final String count = tokenizer.nextToken();
            final String first = tokenizer.nextToken();
            final String last = tokenizer.nextToken();
            info._setNewsgroup(tokenizer.nextToken());
            try {
                info._setArticleCount(Integer.parseInt(count));
                info._setFirstArticle(Integer.parseInt(first));
                info._setLastArticle(Integer.parseInt(last));
            }
            catch (NumberFormatException e) {
                throw new MalformedServerReplyException("Could not parse newsgroup info.\nServer reply: " + reply);
            }
            info._setPostingPermission(0);
            return;
        }
        throw new MalformedServerReplyException("Could not parse newsgroup info.\nServer reply: " + reply);
    }
    
    private NewsgroupInfo __parseNewsgroupListEntry(final String entry) {
        final NewsgroupInfo result = new NewsgroupInfo();
        final StringTokenizer tokenizer = new StringTokenizer(entry);
        if (tokenizer.countTokens() < 4) {
            return null;
        }
        result._setNewsgroup(tokenizer.nextToken());
        final String last = tokenizer.nextToken();
        final String first = tokenizer.nextToken();
        final String permission = tokenizer.nextToken();
        try {
            final int lastNum = Integer.parseInt(last);
            final int firstNum = Integer.parseInt(first);
            result._setFirstArticle(firstNum);
            result._setLastArticle(lastNum);
            if (firstNum == 0 && lastNum == 0) {
                result._setArticleCount(0);
            }
            else {
                result._setArticleCount(lastNum - firstNum + 1);
            }
        }
        catch (NumberFormatException e) {
            return null;
        }
        switch (permission.charAt(0)) {
            case 'Y':
            case 'y': {
                result._setPostingPermission(2);
                break;
            }
            case 'N':
            case 'n': {
                result._setPostingPermission(3);
                break;
            }
            case 'M':
            case 'm': {
                result._setPostingPermission(1);
                break;
            }
            default: {
                result._setPostingPermission(0);
                break;
            }
        }
        return result;
    }
    
    private NewsgroupInfo[] __readNewsgroupListing() throws IOException {
        final BufferedReader reader = new BufferedReader(new DotTerminatedMessageReader(this._reader_));
        final Vector<NewsgroupInfo> list = new Vector<NewsgroupInfo>(2048);
        String line;
        while ((line = reader.readLine()) != null) {
            final NewsgroupInfo tmp = this.__parseNewsgroupListEntry(line);
            if (tmp == null) {
                throw new MalformedServerReplyException(line);
            }
            list.addElement(tmp);
        }
        final int size;
        if ((size = list.size()) < 1) {
            return new NewsgroupInfo[0];
        }
        final NewsgroupInfo[] info = new NewsgroupInfo[size];
        list.copyInto(info);
        return info;
    }
    
    private Reader __retrieve(final int command, final String articleId, final ArticlePointer pointer) throws IOException {
        if (articleId != null) {
            if (!NNTPReply.isPositiveCompletion(this.sendCommand(command, articleId))) {
                return null;
            }
        }
        else if (!NNTPReply.isPositiveCompletion(this.sendCommand(command))) {
            return null;
        }
        if (pointer != null) {
            this.__parseArticlePointer(this.getReplyString(), pointer);
        }
        final Reader reader = new DotTerminatedMessageReader(this._reader_);
        return reader;
    }
    
    private Reader __retrieve(final int command, final int articleNumber, final ArticlePointer pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.sendCommand(command, Integer.toString(articleNumber)))) {
            return null;
        }
        if (pointer != null) {
            this.__parseArticlePointer(this.getReplyString(), pointer);
        }
        final Reader reader = new DotTerminatedMessageReader(this._reader_);
        return reader;
    }
    
    public Reader retrieveArticle(final String articleId, final ArticlePointer pointer) throws IOException {
        return this.__retrieve(0, articleId, pointer);
    }
    
    public Reader retrieveArticle(final String articleId) throws IOException {
        return this.retrieveArticle(articleId, null);
    }
    
    public Reader retrieveArticle() throws IOException {
        return this.retrieveArticle(null);
    }
    
    public Reader retrieveArticle(final int articleNumber, final ArticlePointer pointer) throws IOException {
        return this.__retrieve(0, articleNumber, pointer);
    }
    
    public Reader retrieveArticle(final int articleNumber) throws IOException {
        return this.retrieveArticle(articleNumber, null);
    }
    
    public Reader retrieveArticleHeader(final String articleId, final ArticlePointer pointer) throws IOException {
        return this.__retrieve(3, articleId, pointer);
    }
    
    public Reader retrieveArticleHeader(final String articleId) throws IOException {
        return this.retrieveArticleHeader(articleId, null);
    }
    
    public Reader retrieveArticleHeader() throws IOException {
        return this.retrieveArticleHeader(null);
    }
    
    public Reader retrieveArticleHeader(final int articleNumber, final ArticlePointer pointer) throws IOException {
        return this.__retrieve(3, articleNumber, pointer);
    }
    
    public Reader retrieveArticleHeader(final int articleNumber) throws IOException {
        return this.retrieveArticleHeader(articleNumber, null);
    }
    
    public Reader retrieveArticleBody(final String articleId, final ArticlePointer pointer) throws IOException {
        return this.__retrieve(1, articleId, pointer);
    }
    
    public Reader retrieveArticleBody(final String articleId) throws IOException {
        return this.retrieveArticleBody(articleId, null);
    }
    
    public Reader retrieveArticleBody() throws IOException {
        return this.retrieveArticleBody(null);
    }
    
    public Reader retrieveArticleBody(final int articleNumber, final ArticlePointer pointer) throws IOException {
        return this.__retrieve(1, articleNumber, pointer);
    }
    
    public Reader retrieveArticleBody(final int articleNumber) throws IOException {
        return this.retrieveArticleBody(articleNumber, null);
    }
    
    public boolean selectNewsgroup(final String newsgroup, final NewsgroupInfo info) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.group(newsgroup))) {
            return false;
        }
        if (info != null) {
            this.__parseGroupReply(this.getReplyString(), info);
        }
        return true;
    }
    
    public boolean selectNewsgroup(final String newsgroup) throws IOException {
        return this.selectNewsgroup(newsgroup, null);
    }
    
    public String listHelp() throws IOException {
        if (!NNTPReply.isInformational(this.help())) {
            return null;
        }
        final StringWriter help = new StringWriter();
        final Reader reader = new DotTerminatedMessageReader(this._reader_);
        Util.copyReader(reader, help);
        reader.close();
        help.close();
        return help.toString();
    }
    
    public boolean selectArticle(final String articleId, final ArticlePointer pointer) throws IOException {
        if (articleId != null) {
            if (!NNTPReply.isPositiveCompletion(this.stat(articleId))) {
                return false;
            }
        }
        else if (!NNTPReply.isPositiveCompletion(this.stat())) {
            return false;
        }
        if (pointer != null) {
            this.__parseArticlePointer(this.getReplyString(), pointer);
        }
        return true;
    }
    
    public boolean selectArticle(final String articleId) throws IOException {
        return this.selectArticle(articleId, null);
    }
    
    public boolean selectArticle(final ArticlePointer pointer) throws IOException {
        return this.selectArticle(null, pointer);
    }
    
    public boolean selectArticle(final int articleNumber, final ArticlePointer pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.stat(articleNumber))) {
            return false;
        }
        if (pointer != null) {
            this.__parseArticlePointer(this.getReplyString(), pointer);
        }
        return true;
    }
    
    public boolean selectArticle(final int articleNumber) throws IOException {
        return this.selectArticle(articleNumber, null);
    }
    
    public boolean selectPreviousArticle(final ArticlePointer pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.last())) {
            return false;
        }
        if (pointer != null) {
            this.__parseArticlePointer(this.getReplyString(), pointer);
        }
        return true;
    }
    
    public boolean selectPreviousArticle() throws IOException {
        return this.selectPreviousArticle(null);
    }
    
    public boolean selectNextArticle(final ArticlePointer pointer) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.next())) {
            return false;
        }
        if (pointer != null) {
            this.__parseArticlePointer(this.getReplyString(), pointer);
        }
        return true;
    }
    
    public boolean selectNextArticle() throws IOException {
        return this.selectNextArticle(null);
    }
    
    public NewsgroupInfo[] listNewsgroups() throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.list())) {
            return null;
        }
        return this.__readNewsgroupListing();
    }
    
    public NewsgroupInfo[] listNewsgroups(final String wildmat) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.listActive(wildmat))) {
            return null;
        }
        return this.__readNewsgroupListing();
    }
    
    public NewsgroupInfo[] listNewNewsgroups(final NewGroupsOrNewsQuery query) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.newgroups(query.getDate(), query.getTime(), query.isGMT(), query.getDistributions()))) {
            return null;
        }
        return this.__readNewsgroupListing();
    }
    
    public String[] listNewNews(final NewGroupsOrNewsQuery query) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.newnews(query.getNewsgroups(), query.getDate(), query.getTime(), query.isGMT(), query.getDistributions()))) {
            return null;
        }
        final Vector<String> list = new Vector<String>();
        final BufferedReader reader = new BufferedReader(new DotTerminatedMessageReader(this._reader_));
        String line;
        while ((line = reader.readLine()) != null) {
            list.addElement(line);
        }
        final int size = list.size();
        if (size < 1) {
            return new String[0];
        }
        final String[] result = new String[size];
        list.copyInto(result);
        return result;
    }
    
    public boolean completePendingCommand() throws IOException {
        return NNTPReply.isPositiveCompletion(this.getReply());
    }
    
    public Writer postArticle() throws IOException {
        if (!NNTPReply.isPositiveIntermediate(this.post())) {
            return null;
        }
        return new DotTerminatedMessageWriter(this._writer_);
    }
    
    public Writer forwardArticle(final String articleId) throws IOException {
        if (!NNTPReply.isPositiveIntermediate(this.ihave(articleId))) {
            return null;
        }
        return new DotTerminatedMessageWriter(this._writer_);
    }
    
    public boolean logout() throws IOException {
        return NNTPReply.isPositiveCompletion(this.quit());
    }
    
    public boolean authenticate(final String username, final String password) throws IOException {
        int replyCode = this.authinfoUser(username);
        if (replyCode == 381) {
            replyCode = this.authinfoPass(password);
            if (replyCode == 281) {
                return this._isAllowedToPost = true;
            }
        }
        return false;
    }
    
    private Reader __retrieveArticleInfo(final String articleRange) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.xover(articleRange))) {
            return null;
        }
        return new DotTerminatedMessageReader(this._reader_);
    }
    
    public Reader retrieveArticleInfo(final int articleNumber) throws IOException {
        return this.__retrieveArticleInfo(Integer.toString(articleNumber));
    }
    
    public Reader retrieveArticleInfo(final int lowArticleNumber, final int highArticleNumber) throws IOException {
        return this.__retrieveArticleInfo(lowArticleNumber + "-" + highArticleNumber);
    }
    
    private Reader __retrieveHeader(final String header, final String articleRange) throws IOException {
        if (!NNTPReply.isPositiveCompletion(this.xhdr(header, articleRange))) {
            return null;
        }
        return new DotTerminatedMessageReader(this._reader_);
    }
    
    public Reader retrieveHeader(final String header, final int articleNumber) throws IOException {
        return this.__retrieveHeader(header, Integer.toString(articleNumber));
    }
    
    public Reader retrieveHeader(final String header, final int lowArticleNumber, final int highArticleNumber) throws IOException {
        return this.__retrieveHeader(header, lowArticleNumber + "-" + highArticleNumber);
    }
}
