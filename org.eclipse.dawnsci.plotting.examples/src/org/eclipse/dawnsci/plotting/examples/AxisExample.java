package org.eclipse.dawnsci.plotting.examples;

import java.util.Arrays;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dawnsci.plotting.api.axis.IAxis;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.diamond.scisoft.analysis.dataset.IDataset;
import uk.ac.diamond.scisoft.analysis.dataset.IDatasetMathsService;

/**
 * View which creates a sector selection region
 * and listens to that region moving.
 * 
 * @author fcp94556
 *
 */
public class AxisExample extends XYExample {
	

	public AxisExample() {
		super();			
	}
	
	public void createPartControl(Composite parent) {
		
		super.createPartControl(parent); // plots an image for us
		
		try {
			final IDatasetMathsService mservice = (IDatasetMathsService)Activator.getService(IDatasetMathsService.class);
			
			// Create a 1D dataset programmatically. Can also use 
			final IDataset set = mservice.createRange(0, 100000, 1000, IDatasetMathsService.INT);
			set.setName("Different scale data");
			
			final IAxis otherX = system.createAxis("top", false, SWT.TOP);
			final IAxis otherY = system.createAxis("right", true, SWT.RIGHT);
		    
			system.setSelectedXAxis(otherX);
			system.setSelectedYAxis(otherY);
			
			system.createPlot1D(null, Arrays.asList(set), new NullProgressMonitor());
			
			system.setTitle("Axis Example");

		} catch (Exception e) {
  		    e.printStackTrace(); // Or your favourite logging
		}
		
		
    }
	
	protected String getImageName() {
		return "duke_football.jpg";
	}

}