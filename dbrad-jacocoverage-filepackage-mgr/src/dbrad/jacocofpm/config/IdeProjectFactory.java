///* Copyright (c) 2017 dbradley. All rights reserved. */
//package dbrad.jacocofpm.config;
//
//import java.beans.PropertyChangeListener;
//import java.io.IOException;
//import javax.swing.Icon;
//import org.netbeans.api.project.Project;
//import org.netbeans.api.project.ProjectInformation;
//import org.netbeans.spi.project.ProjectFactory;
//import org.netbeans.spi.project.ProjectState;
//import org.openide.filesystems.FileObject;
//import org.openide.loaders.DataFolder;
//import org.openide.loaders.DataObjectNotFoundException;
//import org.openide.nodes.FilterNode;
//import org.openide.util.Exceptions;
//import org.openide.util.ImageUtilities;
//import org.openide.util.Lookup;
//import org.openide.util.lookup.Lookups;
//import org.openide.util.lookup.ServiceProvider;
//
///**
// * //99
// *
// * @author dbradley
// */
//@ServiceProvider(service = ProjectFactory.class)
//public class IdeProjectFactory implements ProjectFactory {
//
//    @Override
//    public boolean isProject(FileObject projectDirectory) {
//
//        boolean findFolderState = false;
//        // the project directory may be deleted at any time and as such
//        // may be virtual in the service provider environment
//        if (projectDirectory.isVirtual()) {
//            // is missing, so not a project
//            return findFolderState;
//        }
//        try {
//            findFolderState = FileObject.findFolder(projectDirectory) != null;
//        } catch (IllegalArgumentException e) {
//            // this may happen if the folder is no more and a race condition (guess
//            // this is the case)
//            findFolderState = false;
//        }
//        return findFolderState;
//    }
//
//    @Override
//    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
//        return isProject(dir) ? new FolderProject(dir) : null;
//    }
//
//    @Override
//    public void saveProject(Project prjct) throws IOException, ClassCastException {
//        // leave unimplemented for the moment
//    }
//
//    private class FolderProject implements Project {
//
//        private final FileObject projectDir;
//        private Lookup lkp;
//
//        private FolderProject(FileObject dir) {
//            this.projectDir = dir;
//        }
//
//        @Override
//        public FileObject getProjectDirectory() {
//            return projectDir;
//        }
//
//        @Override
//        public Lookup getLookup() {
//            if (lkp == null) {
//                lkp = Lookups.fixed(new Object[]{
//                    new Info(),});
//            }
//            return lkp;
//        }
//
//        private final class Info implements ProjectInformation {
//
//            @Override
//            public Icon getIcon() {
//                Icon icon = null;
//                try {
//                    icon = ImageUtilities.image2Icon(
//                            new FilterNode(DataFolder.find(
//                                    getProjectDirectory()).getNodeDelegate()).getIcon(1));
//                } catch (DataObjectNotFoundException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//                return icon;
//            }
//
//            @Override
//            public String getName() {
//                return getProjectDirectory().getName();
//            }
//
//            @Override
//            public String getDisplayName() {
//                return getName();
//            }
//
//            @Override
//            public void addPropertyChangeListener(PropertyChangeListener pcl) {
//                //do nothing, won't change
//            }
//
//            @Override
//            public void removePropertyChangeListener(PropertyChangeListener pcl) {
//                //do nothing, won't change
//            }
//
//            @Override
//            public Project getProject() {
//                return FolderProject.this;
//            }
//        }
//    }
//}
