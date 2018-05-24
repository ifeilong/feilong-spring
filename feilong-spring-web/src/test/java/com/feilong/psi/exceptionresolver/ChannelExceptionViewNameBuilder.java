/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.psi.exceptionresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;

import com.feilong.spring.web.servlet.handler.ExceptionViewNameBuilder;

/**
 * 通道发生异常视图构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 * @deprecated 可能不需要, 使用 {@link org.springframework.web.servlet.handler.SimpleMappingExceptionResolver} 即可
 */
@Deprecated
public class ChannelExceptionViewNameBuilder implements ExceptionViewNameBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.psi.exceptionresolver.ExceptionViewNameBuilder#buildViewName(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public String build(HandlerMethod handlerMethod,Exception ex,HttpServletRequest request,HttpServletResponse response){
        //        //安全签名参数不正确的异常.  0 安全码不匹配 #22039
        //        if (ClassUtil.isInstance(ex, SignNotEqualsException.class)){
        //            return payUrlBuilder.to404Page();
        //        }
        //
        //        //---------------------------------------------------------------
        //        //交易没有订单列表.
        //        if (ClassUtil.isInstance(ex, OrderDataListEmptyException.class)){
        //            return payUrlBuilder.to404Page();
        //        }
        //
        //        //---------------------------------------------------------------
        //        //订单状态不可以去交易的异常
        //        if (ClassUtil.isInstance(ex, OrderStatusCanNotPayException.class)){
        //            return payUrlBuilder.to404Page();
        //        }
        //
        //        //---------------------------------------------------------------
        //        // 1 支付交易不存在, 用户随便拼接支付参数  #22035
        //        if (ClassUtil.isInstance(ex, TradeNotExistException.class)){
        //            return payUrlBuilder.to404Page();
        //        }
        //
        //        //---------------------------------------------------------------
        //        // 不是他自己的订单.  #22035
        //        if (ClassUtil.isInstance(ex, TradeNotSelfException.class)){
        //            return payUrlBuilder.to404Page();
        //        }
        //
        //        //---------------------------------------------------------------
        //        //交易的支付类型是 blank的异常
        //        // 出现场景,人为修改数据库为null 或者程序出现漏洞没有将支付类型更新到数据库
        //        if (ClassUtil.isInstance(ex, TradePaymentTypeBlankException.class)){
        //            return payUrlBuilder.to404Page();
        //        }
        //
        //        //---------------------------------------------------------------
        //        //交易的状态不能去支付的异常
        //        if (ClassUtil.isInstance(ex, TradeStatusCanNotPayException.class)){
        //            return payUrlBuilder.to404Page();
        //        }

        //---------------------------------------------------------------
        return null;
    }

    //---------------------------------------------------------------
    //            case PAYMENTADAPTOR_ISNULL:// 找不到支付适配器,或者支付类型不支持,出现场景,人为修改数据库或者程序漏洞未将数据插入数据库.
    //                throw new NetpayException("paymentAdapter is null,tradeId:{}", tradeNo);
    //
    //            case CLICKPAY_BUTTON_MORETIMES:// 点击超过次数.
    //                // #22178
    //                return payUrlBuilder.gotoFailPage(PaymentResult.FAIL3_CLICK_MORE_TIMES);
    //
    //            case TRADE_IS_PENDING:// System is confirming your payment with the bank, please wait a second...
    //                // #24219
    //                return payUrlBuilder.gotoFailPage(PaymentResult.FAIL4_PENDING);
    //
    //            case TRADE_PARTORDER_CANNOT_GOTOPAID:// 交易里面的部分订单不能去支付.
    //                // #22038
    //                return payUrlBuilder.gotoFailPage(PaymentResult.FAIL2_CANNOT_PAY_AGAIN);
    //
    //            case TRADE_CREATE_TRADENO_EXCEPTION:// 创建交易号异常.
    //                return payUrlBuilder.gotoFailPage(PaymentResult.FAIL1_CAN_PAY_AGAIN);
    //
    //            case TRADE_HAS_PAID:// 交易已经被支付了.
    //                // #22037
    //                PayResultEntity payResultEntity = new PayResultEntity(PaymentResult.SUCCESS_ALREADY_PAID);
    //                return payUrlBuilder.gotoSuccessPage(payResultEntity);
    //
    //            case TRADE_HAS_CANCEL:// 已经取消了的
    //                return payUrlBuilder.gotoFailPage(PaymentResult.FAIL2_CANNOT_PAY_AGAIN);
    //
    //            case TRADE_DOESNT_HAS_CANPAIDORDER:// 交易没有可支付的订单.
    //                throw new NetpayException("orderDtoList isNullOrEmpty,tradeId:" + tradeNo);
    //
    //                //            case TRADE_ORDERSLENGTH_NOTEQAULS:// 交易里面的订单和可以支付的订单数量不匹配.
    //                //                throw new NetpayException("TradeOrdersLengthNotEqauls,tradeId:" + tradeNo);
    //
    //            case HAVE_GOT_ATM_CODE:// 已经获得过ATM code.
    //                return "" + request.getAttribute(PayConstants.REQUEST_ATTRIBUTE_NAME_REDIRECT_PATH);
    //
    //            //            case TRADE_IS_KLIKBCA:// KLIKBCA
    //            //                return "" + request.getAttribute(PayConstants.REQUEST_ATTRIBUTE_NAME_REDIRECT_PATH);
    //
}
