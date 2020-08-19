package xtrem.wellworthhawkeye;

import java.io.Serializable;

/**
 * <h1>NetProcess</h1>
 * NetProcess Interface which contains method process provides parameter result that has String
 * NetProcess extends Serializable for NIO Processing
 *
 * @author Azim Charaniya
 * @version 1.0
 * @since 2014-10-31
 */
public interface NetProcess extends Serializable {

    /**
     * process method is implemented  by a class
     *
     * @param result             String which passed the arguments
     * @param http_response_code if ok Then it is 200 or java.net.HttpURLConnection.HTTP_OK or else it is an error
     *                           there is no return type ie void
     */
    public void process(String result, int http_response_code);

}
