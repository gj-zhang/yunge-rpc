package com.rancho.yunge.communicate;

import com.rancho.yunge.context.ClientContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zgj-18063794
 * @createTime 2019-01-21 17:03
 */
public abstract class AbstractClient implements Client {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);

    protected volatile ClientContextHolder clientContextHolder;

    @Override
    public void init(ClientContextHolder clientContextHolder) {
        this.clientContextHolder = clientContextHolder;
    }
}
