/**
 * Copyright (c) 2015 Bookmate.
 * All Rights Reserved.
 *
 * Author: Dmitry Gordeev <netimen@dreamindustries.co>
 * Date:   17.03.15
 */
package netimen.com.demo.api.events;

import com.bookmate.bus.Bus;

import java.util.List;

public class ArePublic extends Bus.Request<List<Boolean>> {
}
