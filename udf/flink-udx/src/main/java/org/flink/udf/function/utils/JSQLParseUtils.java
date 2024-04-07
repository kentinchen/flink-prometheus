package org.flink.udf.function.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.values.ValuesStatement;

public class JSQLParseUtils {
    public static void main(String[] args) {
        String sql = "SELECT `conversation_member`.`id`\nFROM `conversation_member_ZswF_0918` AS `conversation_member`\nWHERE ((`conversation_member`.`cid` = '2434595:65670460') AND (`conversation_member`.`uid` = '65670460@zzd.zwdingding'))\nORDER BY `conversation_member`.`id`\nLIMIT 1";
        long begin = System.currentTimeMillis();
        parseToPlaceholder(sql);
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

    public static String parseToPlaceholder(String sql) {
        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(sql);
            if (statement instanceof Select) {
                parseSelectBody(((Select) statement).getSelectBody());
            } else if (!(statement instanceof net.sf.jsqlparser.statement.insert.Insert)) {
                if (statement instanceof Delete) {
                    parseExpression(((Delete) statement).getWhere());
                } else if (statement instanceof Update && (
                        (Update) statement).getExpressions() != null) {
                    ((Update) statement).getExpressions().forEach(JSQLParseUtils::parseExpression);
                    parseExpression(((Update) statement).getWhere());
                }
            }
            return statement.toString();
        } catch (JSQLParserException e) {
            return sql;
        }
    }

    public static void parseSelectBody(SelectBody selectBody) {
        if (selectBody == null)
            return;
        if (selectBody instanceof WithItem) {
            parseSelectBody(((WithItem) selectBody).getSelectBody());
        } else if (selectBody instanceof SetOperationList) {
            if (((SetOperationList) selectBody).getSelects() != null)
                ((SetOperationList) selectBody).getSelects().forEach(JSQLParseUtils::parseSelectBody);
        } else if (selectBody instanceof ValuesStatement) {
            ((ValuesStatement) selectBody).getExpressions();
        } else if (selectBody instanceof PlainSelect) {
            parseExpression(((PlainSelect) selectBody).getWhere());
            parseFromItem(((PlainSelect) selectBody).getFromItem());
        }
    }

    public static void parseFromItem(FromItem fromItem) {
        if (fromItem == null)
            return;
        if (fromItem instanceof SubSelect) {
            parseSelectBody(((SubSelect) fromItem).getSelectBody());
        } else if (fromItem instanceof SpecialSubSelect && (
                (SpecialSubSelect) fromItem).getSubSelect() != null) {
            parseSelectBody(((SpecialSubSelect) fromItem).getSubSelect().getSelectBody());
        }
    }

    public static void parseExpression(Expression expression) {
        if (expression == null)
            return;
        if (expression instanceof Parenthesis && ((Parenthesis) expression)
                .getExpression() instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) ((Parenthesis) expression).getExpression();
            ((BinaryExpression) ((Parenthesis) expression).getExpression()).setRightExpression(
                    replaceExpression(binaryExpression.getRightExpression()));
            ((BinaryExpression) ((Parenthesis) expression).getExpression()).setLeftExpression(
                    replaceExpression(binaryExpression.getLeftExpression()));
            parseExpression(((BinaryExpression) ((Parenthesis) expression).getExpression()).getRightExpression());
            parseExpression(((BinaryExpression) ((Parenthesis) expression).getExpression()).getLeftExpression());
            return;
        }
        if (expression instanceof BinaryExpression) {
            BinaryExpression binaryExpression = (BinaryExpression) expression;
            ((BinaryExpression) expression).setRightExpression(
                    replaceExpression(binaryExpression.getRightExpression()));
            ((BinaryExpression) expression).setLeftExpression(replaceExpression(binaryExpression.getLeftExpression()));
            parseExpression(((BinaryExpression) expression).getRightExpression());
            parseExpression(((BinaryExpression) expression).getLeftExpression());
        }
    }

    private static Expression replaceExpression(Expression expression) {
        LongValue value = new LongValue("?");
        if (expression instanceof LongValue || expression instanceof net.sf.jsqlparser.expression.StringValue || expression instanceof net.sf.jsqlparser.expression.HexValue || expression instanceof net.sf.jsqlparser.expression.DoubleValue)
            return (Expression) value;
        return expression;
    }
}
