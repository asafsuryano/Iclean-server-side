package acs.demo;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import acs.ActionBoundaryClasses.ActionBoundary;


@RestController
public class ActionController {
	@RequestMapping(path="/acs/actions",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Map<String,Object> createNewUser(@RequestBody  ActionBoundary user) {
		return Collections.singletonMap("type", "blabla");
	}
}
