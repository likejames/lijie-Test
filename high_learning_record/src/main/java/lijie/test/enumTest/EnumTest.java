package lijie.test.enumTest;

import java.util.concurrent.Callable;

/**
 * @author : lj
 * @since : 2021/2/1
 */
public enum EnumTest implements WechatMsgTypeJudge {
    RED{
        @Override
        public String judge(String name) {
            return name;
        }
    }
}
