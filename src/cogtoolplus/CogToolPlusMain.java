/*******************************************************************************
 * CogTool+ Copyright Notice and Distribution Terms
 * CogTool+ 1.0, Copyright (c) 2017-2018 University of Surrey
 * CogTool+ is an implementation of a system framework supporting parameterization and automation of cognitive modelling
 * based on open source software CogTool. 
 * 
 * CogTool+ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * CogTool+ makes use of several third-party components, with the 
 * following notices:
 * 
 * CogTool 1.3
 * JFreeChart 1.0.19
 * Processing 3.3.5
 * Eclipse SWT version 3.448
 * Eclipse GEF Draw2D version 3.2.1
 * 
 * @author Haiyue Yuan, University of Surrey, 2020
 * 
 * Unless otherwise indicated, all Content made available by the Eclipse 
 * Foundation is provided to you under the terms and conditions of the Eclipse 
 * Public License Version 1.0 ("EPL"). A copy of the EPL is provided with this 
 * Content and is also available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * CLISP version 2.38
 * 
 * Copyright (c) Sam Steingold, Bruno Haible 2001-2006
 * This software is distributed under the terms of the FSF Gnu Public License.
 * See COPYRIGHT file in clisp installation folder for more information.
 * 
 * ACT-R 6.0
 * 
 * Copyright (c) 1998-2007 Dan Bothell, Mike Byrne, Christian Lebiere & 
 *                         John R Anderson. 
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License (see LGPL.txt).
 * 
 * Apache Jakarta Commons-Lang 2.1
 * 
 * This product contains software developed by the Apache Software Foundation
 * (http://www.apache.org/)
 * 
 * jopt-simple version 1.0
 * 
 * Copyright (c) 2018 Paul R. Holser, Jr.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Mozilla XULRunner 1.9.0.5
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/.
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The J2SE(TM) Java Runtime Environment version 5.0
 * 
 * Copyright 2009 Sun Microsystems, Inc., 4150
 * Network Circle, Santa Clara, California 95054, U.S.A.  All
 * rights reserved. U.S.  
 * See the LICENSE file in the jre folder for more information.
 ******************************************************************************/


package cogtoolplus;

import java.io.IOException;
import org.xml.sax.SAXException;
import java.util.concurrent.ExecutionException;

public class CogToolPlusMain {
	public static void main(String args[])
			throws SecurityException, IOException, SAXException, InterruptedException, ExecutionException {
		
		// Please update YOUR_OWN_PATH to run the example provided with this repository
		String conf_file = "YOUR_OWN_PATH/example/pin_study/configure.xml"; // This configure file is a XML file containing all paths for mixed model, meta model and parameters for visualisation
		String prj_file = "mixed_models.xml";
		String vis_file = "analyse.xml";
		
		final long startTime = System.currentTimeMillis();

		// Initiate a LaunchCogToolPlus class 
		LaunchCogToolPlus launcher = new LaunchCogToolPlus(conf_file, prj_file, vis_file);
		launcher.createCogToolModels();
		launcher.computeMetaModel(true);

		log("Done.");
		final long endTime = System.currentTimeMillis();
		System.out.println("Total execution time: " + (endTime - startTime));		
	}
  
	private static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}

}
